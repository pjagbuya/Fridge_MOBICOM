package com.mobdeve.agbuya.hallar.hong.fridge.Room
import androidx.room.Embedded
import androidx.room.Relation

//inventory with everything:
data class InventoryWithEverything(
    @Embedded val inventory: InventoryEntity,
    @Relation(
        entity = ContainerEntity::class,
        parentColumn = "inventoryId",
        entityColumn = "inventoryOwnerId"
    )
    val containers: List<ContainerWithIngredients>,

    @Relation(
        entity = RecipeEntity::class,
        parentColumn = "inventoryId",
        entityColumn = "inventoryOwnerId"
    )
    val recipes: List<RecipeWithIngredients>
)

//inventory with its containers
data class InventoryWithContainers(
    @Embedded val inventory: InventoryEntity,
    @Relation(
        parentColumn = "inventoryId",
        entityColumn = "inventoryOwnerId"
    )
    val containers: List<ContainerEntity>
)

//container with its ingredients
data class ContainerWithIngredients(
    @Embedded val container: ContainerEntity,
    @Relation(
        parentColumn = "containerId",
        entityColumn = "attachedContainerId"
    )
    val ingredients: List<IngredientEntity>
)

//inventory with its recipes
data class InventoryWithRecipes(
    @Embedded val inventory: InventoryEntity,

    @Relation(
        parentColumn = "inventoryId",
        entityColumn = "inventoryOwnerId"
    )
    val recipes: List<RecipeEntity>
)

//recipe with its ingredients
data class RecipeWithIngredients(
    @Embedded val recipe: RecipeEntity,
    @Relation(
        parentColumn = "RecipeId",
        entityColumn = "recipeId"
    )
    val ingredients: List<RecipeIngredientEntity>
)
