package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RECIPE_INGREDIENTS")
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true) val ingredientId: Int = 0,
    val recipeId: Int,  // fk points to RecipeEntity.id
    val name: String,
    val amount: Double,
    val unit: String,
    val isCustom: Boolean = false
)
