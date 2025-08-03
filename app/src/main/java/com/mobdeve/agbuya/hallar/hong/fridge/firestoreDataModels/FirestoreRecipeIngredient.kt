package com.mobdeve.agbuya.hallar.hong.fridge.firestoreDataModels

data class FirestoreRecipeIngredient(
    val ingredientId: Int = 0,
    val recipeId: Int = 0,
    val name: String = "",
    val amount: Double = 0.0,
    val unit: String = "",
    val isCustom: Boolean = true
)

