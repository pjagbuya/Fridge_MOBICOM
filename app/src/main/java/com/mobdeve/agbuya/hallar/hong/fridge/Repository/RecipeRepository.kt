package com.mobdeve.agbuya.hallar.hong.fridge.repository

import com.mobdeve.agbuya.hallar.hong.fridge.Mappers.toEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Mappers.toModel
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeDao
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
    suspend fun insertRecipe(recipe: RecipeModel) = withContext(Dispatchers.IO) {
        val recipeId = recipeDao.insertRecipe(recipe.toEntity()).toInt()
//        recipeDao.insertIngredients(recipe.ingredients.map { it.toEntity(recipeId) })
    }

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

    suspend fun assignUserAuthId(fireAuthId: String?){
        return recipeDao.assignFireAuthId(fireAuthId)
    }


}