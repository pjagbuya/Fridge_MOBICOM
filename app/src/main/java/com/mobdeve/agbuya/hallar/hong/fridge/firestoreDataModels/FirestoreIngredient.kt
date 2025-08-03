package com.mobdeve.agbuya.hallar.hong.fridge.firestoreDataModels

data class FirestoreIngredient(
    val ingredientID: Int = 0,
    val name: String = "",
    val iconResId: Int = 0,
    val quantity: Double = 0.0,
    val price: Double = 0.0,
    val unit: String = "",
    val conditionType: String = "",
    val itemType: String = "",
    val dateAdded: String = "",
    val expirationDate: String = "",
    val attachedContainerId: Int = 0,
    val imageList: List<FirestoreImageRaw> = emptyList()
)

