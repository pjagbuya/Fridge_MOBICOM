package com.mobdeve.agbuya.hallar.hong.fridge.Repository

import com.mobdeve.agbuya.hallar.hong.fridge.dao.IngredientDao
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
import kotlinx.coroutines.flow.map
import com.mobdeve.agbuya.hallar.hong.fridge.Mappers.toRecipeIngredient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RecipeIngredientRepository(private val ingredientDao: IngredientDao) {
    fun getAllIngredientsAsRecipeIngredients(): Flow<List<RecipeModel.RecipeIngredient>> {
        return ingredientDao.getAllIngredients().map { list ->
            list.map { it.toRecipeIngredient() }
        }
    }


}