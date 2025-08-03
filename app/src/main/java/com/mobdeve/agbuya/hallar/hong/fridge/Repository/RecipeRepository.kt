package com.mobdeve.agbuya.hallar.hong.fridge.repository

import com.mobdeve.agbuya.hallar.hong.fridge.Mappers.toEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Mappers.toModel
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeDao
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeIngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepository @Inject constructor(private val recipeDao: RecipeDao) {

    //fetch all recipes from the database as RecipeModel list
    suspend fun getAllRecipes(): List<RecipeModel> = withContext(Dispatchers.IO) {
        val dbRecipes = recipeDao.getAllRecipes()
        dbRecipes.map { entity ->
            val ingredients = recipeDao.getIngredientsForRecipe(entity.id)
            entity.toModel(ingredients)
        }
    }

    //insert a RecipeModel into DB
//    suspend fun insertRecipe(recipe: RecipeModel) = withContext(Dispatchers.IO) {
//        val recipeId = recipeDao.insertRecipe(recipe.toEntity()).toInt()
////        recipeDao.insertIngredients(recipe.ingredients.map { it.toEntity(recipeId) })
//    }

    //seed dummy data (if empty for testing)
    suspend fun seedData(sampleRecipes: List<RecipeModel>) = withContext(Dispatchers.IO) {
        if (recipeDao.getAllRecipes().isEmpty()) {
            sampleRecipes.forEach { insertRecipe(it) }
        }
    }

    suspend fun deleteRecipe(recipe: RecipeModel) {
        recipeDao.deleteIngredientsByRecipeId(recipe.id)
        recipeDao.deleteRecipeById(recipe.id)
    }

    suspend fun assignUserAuthId(fireAuthId: String?) {
        return recipeDao.assignFireAuthId(fireAuthId)
    }

    suspend fun insertIngredientForRecipe(recipeId: Int, ingredient: RecipeModel.RecipeIngredient) {
        // Convert the domain model to Room entity
        val entity = RecipeIngredientEntity(
            name = ingredient.name,
            amount = ingredient.amount, // or use a proper type if quantity is numeric
            unit = ingredient.unit.name,
            isCustom = ingredient.isCustom,
            recipeId = recipeId
        )

        recipeDao.insertIngredient(entity)
    }

    suspend fun insertRecipeWithIngredients(recipeModel: RecipeModel): Int {
        val recipeEntity = RecipeEntity(
            name = recipeModel.name,
            description = recipeModel.description,
        )

        val recipeId = recipeDao.insertRecipe(recipeEntity).toInt()

        val ingredientEntities = recipeModel.ingredients.map { ingredient ->
            RecipeIngredientEntity(
                recipeId = recipeId,
                name = ingredient.name,
                amount = ingredient.amount,
                unit = ingredient.unit.name,
                isCustom = true,
            )
        }

        recipeDao.insertIngredients(ingredientEntities)
        return recipeId // You can use this to update your ViewModel’s currentRecipeId
    }

    suspend fun insertRecipe(recipe: RecipeModel) = withContext(Dispatchers.IO) {
        recipeDao.insertRecipeWithIngredients(
            recipe.toEntity(),
            recipe.ingredients.map { it.toEntity(0) }
        )
    }


    suspend fun saveRecipe(recipe: RecipeModel) = withContext(Dispatchers.IO) {
        val entity = recipe.toEntity()

        if (recipe.id == 0) {
            // insert new recipe + ingredients
            recipeDao.insertRecipeWithIngredients(
                entity,
                recipe.ingredients.map { it.toEntity(0) }
            )
        } else {
            // update existing recipe + replacing its ingredients
            recipeDao.updateRecipe(entity)
            recipeDao.deleteIngredientsByRecipeId(recipe.id)
            val updatedIngredients = recipe.ingredients.map { it.toEntity(recipe.id) }
            recipeDao.insertIngredients(updatedIngredients)
            // recipeDao.insertIngredients(recipe.ingredients.map { it.toEntity(recipe.id) })
        }
    }

    suspend fun getRecipeById(id: Int): RecipeModel = withContext(Dispatchers.IO) {
        val recipe = recipeDao.getRecipeById(id)
        val ingredients = recipeDao.getIngredientsForRecipe(id)
        recipe.toModel(ingredients)
    }

}