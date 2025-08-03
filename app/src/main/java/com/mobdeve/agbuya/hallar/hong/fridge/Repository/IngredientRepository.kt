package com.mobdeve.agbuya.hallar.hong.fridge.Repository

import com.mobdeve.agbuya.hallar.hong.fridge.Mappers.toRecipeIngredient
import com.mobdeve.agbuya.hallar.hong.fridge.Room.IngredientDao
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel

class IngredientRepository(private val ingredientDao: IngredientDao) {
    suspend fun getAllIngredientsAsRecipeIngredients(): List<RecipeModel.RecipeIngredient> {
        return ingredientDao.getAllIngredients().map { it.toRecipeIngredient() }
    }
}