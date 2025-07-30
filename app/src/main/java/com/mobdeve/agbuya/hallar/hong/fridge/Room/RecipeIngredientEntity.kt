package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "recipe_ingredients",
    foreignKeys = [ForeignKey(
        entity = RecipeEntity::class,
        parentColumns = ["RecipeId"],
        childColumns = ["recipeId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["recipeId"])]
    )
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true) val ingredientId: Int = 0,
    val recipeId: Int,  // fk
    val name: String,
    val amount: Double,
    val unit: String,
    val isCustom: Boolean = false
)
