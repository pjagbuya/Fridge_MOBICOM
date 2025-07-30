package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeIngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipeIngredient: RecipeIngredientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ingredients: List<RecipeIngredientEntity>)

    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    suspend fun getIngredientsForRecipe(recipeId: Int): List<RecipeIngredientEntity>

    @Delete
    suspend fun delete(recipeIngredient: RecipeIngredientEntity)

    @Query("DELETE FROM recipe_ingredients WHERE recipeId = :recipeId")
    suspend fun deleteByRecipeId(recipeId: Int)

    @Query("DELETE FROM recipe_ingredients")
    suspend fun deleteAll()
}