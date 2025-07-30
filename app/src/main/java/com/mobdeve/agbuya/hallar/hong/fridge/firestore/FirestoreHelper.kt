package com.mobdeve.agbuya.hallar.hong.fridge.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeIngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.*
import kotlinx.coroutines.tasks.await

object FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()
    private val currentUserUid: String? = FirebaseAuth.getInstance().currentUser?.uid

    suspend fun uploadInventoryFull(inventory: InventoryWithEverything) {
        if (currentUserUid == null) return

        val inventoryRef = db.collection("users")
            .document(currentUserUid)
            .collection("inventories")
            .document(inventory.inventory.inventoryId)

        // Store the main InventoryEntity
        inventoryRef.collection("meta")
            .document("inventory")
            .set(inventory.inventory)
            .await()

        // Store containers
        val containers = inventory.containers.map { it.container }
        containers.forEach {
            inventoryRef.collection("containers")
                .document(it.containerId.toString())
                .set(it)
                .await()
        }

        // Store ingredients from containers
        inventory.containers.forEach { containerWithIngredients ->
            containerWithIngredients.ingredients.forEach { ingredient ->
                inventoryRef.collection("ingredients")
                    .document(ingredient.ingredientID.toString())
                    .set(ingredient)
                    .await()
            }
        }

        // Store recipes
        inventory.recipes.map { it.recipe }.forEach { recipe ->
            inventoryRef.collection("recipes")
                .document(recipe.RecipeId.toString())
                .set(recipe)
                .await()
        }

        // Store recipe ingredients
        inventory.recipes.forEach { recipeWithIngredients ->
            recipeWithIngredients.ingredients.forEach { ingredient ->
                inventoryRef.collection("recipe_ingredients")
                    .document(ingredient.ingredientId.toString())
                    .set(ingredient)
                    .await()
            }
        }
    }

    suspend fun downloadInventoryFull(inventoryId: String): InventoryWithEverything? {
        if (currentUserUid == null) return null

        val inventoryRef = db.collection("users")
            .document(currentUserUid)
            .collection("inventories")
            .document(inventoryId)

        val inventorySnap = inventoryRef.collection("meta").document("inventory").get().await()
        val inventory = inventorySnap.toObject(InventoryEntity::class.java) ?: return null

        val containerSnapshots = inventoryRef.collection("containers").get().await()
        val containers = containerSnapshots.toObjects(ContainerEntity::class.java)

        val ingredientSnapshots = inventoryRef.collection("ingredients").get().await()
        val ingredients = ingredientSnapshots.toObjects(IngredientEntity::class.java)

        val recipeSnapshots = inventoryRef.collection("recipes").get().await()
        val recipes = recipeSnapshots.toObjects(RecipeEntity::class.java)

        val recipeIngredientSnapshots = inventoryRef.collection("recipe_ingredients").get().await()
        val recipeIngredients = recipeIngredientSnapshots.toObjects(RecipeIngredientEntity::class.java)

        val containersWithIngredients = containers.map { container ->
            val containerIngredients = ingredients.filter { it.attachedContainerId == container.containerId }
            ContainerWithIngredients(container, containerIngredients)
        }

        val recipesWithIngredients = recipes.map { recipe ->
            val ingredientsForRecipe = recipeIngredients.filter { it.recipeId == recipe.RecipeId }
            RecipeWithIngredients(recipe, ingredientsForRecipe)
        }

        return InventoryWithEverything(
            inventory = inventory,
            containers = containersWithIngredients,
            recipes = recipesWithIngredients
        )
    }
}
