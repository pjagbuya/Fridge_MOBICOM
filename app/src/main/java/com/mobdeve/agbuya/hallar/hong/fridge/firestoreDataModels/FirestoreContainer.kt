package com.mobdeve.agbuya.hallar.hong.fridge.firestoreDataModels

data class FirestoreContainer(
    val containerId: Int = 0,
    val name: String = "",
    val imageContainer: FirestoreImageContainer = FirestoreImageContainer(),
    val currCap: Int = 0,
    val maxCap: Int = 0,
    val timeStamp: String = "",
    val ownerUserId: Int = 0,
    val fireAuthId: String? = null
)

