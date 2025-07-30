package com.mobdeve.agbuya.hallar.hong.fridge.dao

import androidx.room.*
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeIngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.RecipeWithIngredients

@Dao
interface RecipeDao {

    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Insert
    suspend fun insertIngredients(ingredients: List<RecipeIngredientEntity>)

    @Transaction
    suspend fun insertRecipeWithIngredients(recipe: RecipeEntity, ingredients: List<RecipeIngredientEntity>) {
        val recipeId = insertRecipe(recipe).toInt()
        insertIngredients(ingredients.map { it.copy(recipeId = recipeId) })
    }


    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<RecipeEntity>

    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    suspend fun getIngredientsForRecipe(recipeId: Int): List<RecipeIngredientEntity>

    @Transaction
    @Query("SELECT * FROM recipes WHERE RecipeId = :recipeId")
    suspend fun getRecipeWithIngredients(recipeId: Int): RecipeWithIngredients

    @Query("DELETE FROM recipes WHERE RecipeId = :recipeId")
    suspend fun deleteRecipeById(recipeId: Int)

    @Query("DELETE FROM recipe_ingredients WHERE recipeId = :recipeId")
    suspend fun deleteIngredientsByRecipeId(recipeId: Int)
}
