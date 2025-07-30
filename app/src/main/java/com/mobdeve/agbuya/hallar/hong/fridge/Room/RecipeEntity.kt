package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.InventoryEntity
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "recipes",
    foreignKeys = [ForeignKey(
        entity = InventoryEntity::class,
        parentColumns = ["inventoryId"],
        childColumns = ["inventoryOwnerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("inventoryOwnerId")]

)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val RecipeId: Int = 0,
    val inventoryOwnerId: String,  //fk
    val name: String,
    val description: String = ""
)
