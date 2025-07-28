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

//    @TypeConverter
//    fun fromImageList(value: List<ByteArray>): String {
//        return Gson().toJson(value)
//    }
//
//    @TypeConverter
//    fun toImageList(value: String): List<ByteArray> {
//        val type = object : TypeToken<List<ByteArray>>() {}.type
//        return Gson().fromJson(value, type)
//    }
}