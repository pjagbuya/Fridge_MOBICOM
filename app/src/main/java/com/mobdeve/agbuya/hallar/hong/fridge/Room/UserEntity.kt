package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val firebaseUid: String, // Fireauth current user  RecipeId
    val username: String,

    //added
    val ownedInventoryId: String?,
    val joinedInventoryId: String?
)
