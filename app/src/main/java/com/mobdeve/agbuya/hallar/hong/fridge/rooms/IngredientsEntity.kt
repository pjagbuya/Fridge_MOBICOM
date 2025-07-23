package com.mobdeve.agbuya.hallar.hong.fridge.rooms

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageRaw

@Entity(
    foreignKeys = [ForeignKey(
        entity = ContainerEntity::class,
        parentColumns = ["containerId"],
        childColumns = ["attachedContainerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true) val ingredientId: Int = 0,
    val name: String,
    val iconResId: Int,
    val amount: Double,
    val unit: String,
    val conditionType: String,
    val itemType: String,
    val dateAdded: String,
    val expirationDate: String,
    val attachedContainerId: Int,
    val imageList: ArrayList<ImageRaw>
)