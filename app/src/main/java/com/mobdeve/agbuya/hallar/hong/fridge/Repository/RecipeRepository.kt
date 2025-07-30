package com.mobdeve.agbuya.hallar.hong.fridge.repository

import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeIngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeWithIngredients
import com.mobdeve.agbuya.hallar.hong.fridge.dao.RecipeDao

class RecipeRepository(private val recipeDao: RecipeDao) {

    suspend fun insertRecipe(recipe: RecipeEntity): Long {
        return recipeDao.insertRecipe(recipe)
    }

    suspend fun insertIngredients(ingredients: List<RecipeIngredientEntity>) {
        recipeDao.insertIngredients(ingredients)
    }

    suspend fun insertRecipeWithIngredients(recipe: RecipeEntity, ingredients: List<RecipeIngredientEntity>) {
        recipeDao.insertRecipeWithIngredients(recipe, ingredients)
    }

    suspend fun getAllRecipes(): List<RecipeEntity> {
        return recipeDao.getAllRecipes()
    }

    suspend fun getIngredientsForRecipe(recipeId: Int): List<RecipeIngredientEntity> {
        return recipeDao.getIngredientsForRecipe(recipeId)
    }

    suspend fun getRecipeWithIngredients(recipeId: Int): RecipeWithIngredients {
        return recipeDao.getRecipeWithIngredients(recipeId)
    }

    suspend fun deleteRecipeById(recipeId: Int) {
        recipeDao.deleteRecipeById(recipeId)
    }

    suspend fun deleteIngredientsByRecipeId(recipeId: Int) {
        recipeDao.deleteIngredientsByRecipeId(recipeId)
    }
}
