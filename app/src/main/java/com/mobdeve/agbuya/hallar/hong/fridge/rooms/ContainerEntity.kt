package com.mobdeve.agbuya.hallar.hong.fridge.rooms

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer
import com.mobdeve.agbuya.hallar.hong.fridge.converters.ImageContainerTypeConverter
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserEntity
import kotlinx.parcelize.Parcelize



@Parcelize
@Entity(
//    foreignKeys = [ForeignKey(
//        entity = UserEntity::class,
//        parentColumns = ["id"],
//        childColumns = ["ownerUserId"],
//        onDelete = ForeignKey.CASCADE
//    )]
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
): Parcelable