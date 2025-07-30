package com.mobdeve.agbuya.hallar.hong.fridge.Repository

import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeIngredientDao
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeIngredientEntity

class RecipeIngredientRepository(private val recipeIngredientDao: RecipeIngredientDao) {

    suspend fun insert(recipeIngredient: RecipeIngredientEntity) {
        recipeIngredientDao.insert(recipeIngredient)
    }

    suspend fun insertAll(ingredients: List<RecipeIngredientEntity>) {
        recipeIngredientDao.insertAll(ingredients)
    }

    suspend fun getIngredientsForRecipe(recipeId: Int): List<RecipeIngredientEntity> {
        return recipeIngredientDao.getIngredientsForRecipe(recipeId)
    }

    suspend fun delete(recipeIngredient: RecipeIngredientEntity) {
        recipeIngredientDao.delete(recipeIngredient)
    }

    suspend fun deleteByRecipeId(recipeId: Int) {
        recipeIngredientDao.deleteByRecipeId(recipeId)
    }
}