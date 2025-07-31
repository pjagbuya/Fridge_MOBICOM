package com.mobdeve.agbuya.hallar.hong.fridge.Mappers

import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeIngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel

// <----- Mappers for Recipe ----->

// RecipeModel to RecipeEntity
fun RecipeModel.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = this.id, // if (this.id == 0) 0 else
        name = this.name,
        description = this.description
    )
}

// RecipeIngredient to RecipeIngredientEntity
fun RecipeModel.RecipeIngredient.toEntity(recipeId: Int): RecipeIngredientEntity {
    return RecipeIngredientEntity(
        ingredientId = if (this.ingredientID == 0) 0 else this.ingredientID,
        recipeId = recipeId,
        name = this.name,
        amount = this.amount,
        unit = this.unit.displayName,
        isCustom = this.isCustom
    )
}

// RecipeEntity + RecipeIngredientEntity to RecipeModel
fun RecipeEntity.toModel(ingredients: List<RecipeIngredientEntity>): RecipeModel {
    return RecipeModel(
        id = this.id,
        name = this.name,
        description = this.description,
        ingredients = ArrayList(
            ingredients.map {
                RecipeModel.RecipeIngredient(
                    ingredientID = it.ingredientId,
                    name = it.name,
                    amount = it.amount,
                    unit = RecipeModel.RecipeUnit.entries.find { u -> u.displayName == it.unit }
                        ?: RecipeModel.RecipeUnit.UNSPECIFIED,
                    isCustom = it.isCustom
                )
            }
        )
    )
}