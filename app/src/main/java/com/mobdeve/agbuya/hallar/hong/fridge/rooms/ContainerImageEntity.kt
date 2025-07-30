package com.mobdeve.agbuya.hallar.hong.fridge.rooms

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class ContainerImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ingredientOwnerId: Int,
    val colorId: Int
)