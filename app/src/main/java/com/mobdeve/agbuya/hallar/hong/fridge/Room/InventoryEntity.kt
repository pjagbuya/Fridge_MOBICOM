package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.*

@Entity (indices = [Index(value = ["inventoryId"], unique = true)])
data class InventoryEntity(
    @PrimaryKey val inventoryId: String,
    val userAuthId: String? = null, //user's fireAuthId if logged in
    val userId : Int, //user if not logged in
    val name: String
)