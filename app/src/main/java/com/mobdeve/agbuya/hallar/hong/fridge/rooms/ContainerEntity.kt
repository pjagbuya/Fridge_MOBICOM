package com.mobdeve.agbuya.hallar.hong.fridge.rooms

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer
import com.mobdeve.agbuya.hallar.hong.fridge.converters.ImageContainerTypeConverter

@Entity(
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["userId"],
        childColumns = ["ownerUserId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ContainerEntity(
    @PrimaryKey(autoGenerate = true) val containerId: Int = 0,
    val name: String,

    @TypeConverters(ImageContainerTypeConverter::class)
    val imageContainer: ImageContainer,
    var currCap: Int,
    val maxCap: Int,
    val timeStamp: String,
    val ownerUserId: Int
)