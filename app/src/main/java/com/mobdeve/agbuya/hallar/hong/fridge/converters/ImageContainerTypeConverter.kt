package com.mobdeve.agbuya.hallar.hong.fridge.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer

class ImageContainerTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromImageContainer(container: ImageContainer): String {
        return gson.toJson(container)
    }

    @TypeConverter
    fun toImageContainer(json: String): ImageContainer {
        return gson.fromJson(json, ImageContainer::class.java)
    }
}