package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction


@Dao
interface RecipeDao {
    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Insert
    suspend fun insertIngredients(ingredients: List<RecipeIngredientEntity>)

    @Transaction
    suspend fun insertRecipeWithIngredients(
        recipe: RecipeEntity,
        ingredients: List<RecipeIngredientEntity>
    ) {
        val recipeId = insertRecipe(recipe).toInt()
        insertIngredients(ingredients.map { it.copy(recipeId = recipeId) })
    }

    //get all recipes
    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<RecipeEntity>

    //get ingredients of a specific recipe
    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    suspend fun getIngredientsForRecipe(recipeId: Int): List<RecipeIngredientEntity>

//     delete a recipe by ID
    @Query("DELETE FROM recipes WHERE id = :recipeId")
    suspend fun deleteRecipeById(recipeId: Int)

//    also delete the recipe's ingredient when deleting a recipe
    @Query("DELETE FROM recipe_ingredients WHERE recipeId = :recipeId")
    suspend fun deleteIngredientsByRecipeId(recipeId: Int)
}