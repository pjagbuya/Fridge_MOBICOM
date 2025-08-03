package com.mobdeve.agbuya.hallar.hong.fridge.firestoreDataModels

data class FirestoreRecipe(
    val id: Int = 0,
    val userId: Int = 1,
    val userAuthId: String? = null,
    val name: String = "",
    val description: String = ""
)

