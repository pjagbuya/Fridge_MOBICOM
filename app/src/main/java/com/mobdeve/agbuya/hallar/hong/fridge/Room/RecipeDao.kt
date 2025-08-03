package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.*
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeEntity

@Dao
abstract class RecipeDao {

    @Insert
    abstract suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Insert
    abstract suspend fun insertIngredients(ingredients: List<RecipeIngredientEntity>)

    @Update
    abstract suspend fun updateRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM recipes")
    abstract suspend fun getAllRecipes(): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    abstract suspend fun getRecipeById(recipeId: Int): RecipeEntity

    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    abstract suspend fun getIngredientsForRecipe(recipeId: Int): List<RecipeIngredientEntity>

    @Query("DELETE FROM recipes WHERE id = :recipeId")
    abstract suspend fun deleteRecipeById(recipeId: Int)

    @Query("DELETE FROM recipe_ingredients WHERE recipeId = :recipeId")
    abstract suspend fun deleteIngredientsByRecipeId(recipeId: Int)

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    abstract suspend fun getRecipeWithIngredients(recipeId: Int): RecipeWithIngredients

    @Transaction
    open suspend fun insertRecipeWithIngredients(
        recipe: RecipeEntity,
        ingredients: List<RecipeIngredientEntity>
    ) {
        val recipeId = insertRecipe(recipe).toInt()
        insertIngredients(ingredients.map { it.copy(recipeId = recipeId) })
    }
}