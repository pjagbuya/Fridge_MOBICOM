package com.mobdeve.agbuya.hallar.hong.fridge.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageRaw

class Converters {

    @TypeConverter
    fun fromImageRawList(value: ArrayList<ImageRaw>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toImageRawList(value: String): ArrayList<ImageRaw> {
        val type = object : TypeToken<ArrayList<ImageRaw>>() {}.type
        return Gson().fromJson(value, type)
    }
}