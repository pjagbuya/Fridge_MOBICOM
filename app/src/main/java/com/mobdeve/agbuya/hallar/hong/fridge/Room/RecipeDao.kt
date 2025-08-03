package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update


@Dao
interface RecipeDao {
    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Insert
    suspend fun insertIngredients(ingredients: List<RecipeIngredientEntity>)

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Transaction
    suspend fun insertRecipeWithIngredients(
        recipe: RecipeEntity,
        ingredients: List<RecipeIngredientEntity>
    ) {
        val recipeId = insertRecipe(recipe).toInt()
        insertIngredients(ingredients.map { it.copy(recipeId = recipeId) })
    }

    //get all recipes
    @Query("SELECT * FROM RecipeEntity")
    suspend fun getAllRecipes(): List<RecipeEntity>

    //get ingredients of a specific recipe
    @Query("SELECT * FROM RecipeIngredientEntity WHERE recipeId = :recipeId")
    suspend fun getIngredientsForRecipe(recipeId: Int): List<RecipeIngredientEntity>

    @Query("SELECT * FROM RecipeEntity WHERE id = :recipeId")
    abstract suspend fun getRecipeById(recipeId: Int): RecipeEntity

//     delete a recipe by ID
    @Query("DELETE FROM RecipeEntity WHERE id = :recipeId")
    suspend fun deleteRecipeById(recipeId: Int)

//    also delete the recipe's ingredient when deleting a recipe
    @Query("DELETE FROM RecipeIngredientEntity WHERE recipeId = :recipeId")
    suspend fun deleteIngredientsByRecipeId(recipeId: Int)

    //update authId if null only
    @Query("UPDATE RecipeEntity SET userAuthId = :fireAuthId WHERE userAuthId IS NULL")
    suspend fun assignFireAuthId(fireAuthId: String?)
}