package com.mobdeve.agbuya.hallar.hong.fridge.Room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer
import com.mobdeve.agbuya.hallar.hong.fridge.converters.ImageContainerTypeConverter
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity( tableName = "containers",
    //added
    foreignKeys = [ForeignKey(
        entity = InventoryEntity::class,
        parentColumns = ["inventoryId"],
        childColumns = ["inventoryOwnerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("inventoryOwnerId")]
)
data class ContainerEntity(
    @PrimaryKey(autoGenerate = false) val containerId: Int = 0,
    val inventoryOwnerId: String,
    val name: String,

    @TypeConverters(ImageContainerTypeConverter::class)
    val imageContainer: ImageContainer,
    var currCap: Int,
    val maxCap: Int,
    val timeStamp: String,
//    val ownerUserId: Int
): Parcelable