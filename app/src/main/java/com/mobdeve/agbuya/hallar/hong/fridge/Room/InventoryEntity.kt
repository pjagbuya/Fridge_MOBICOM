package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.*
@Entity
data class InventoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ownerEmail: String,
    val name: String
)