package com.mobdeve.agbuya.hallar.hong.fridge.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageRaw

import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeEntity
import com.mobdeve.agbuya.hallar.hong.fridge.domain.Member

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

    //for inventory
    private val gson = Gson()

    @TypeConverter
    fun fromContainerList(list: List<ContainerEntity>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toContainerList(json: String): List<ContainerEntity> {
        val type = object : TypeToken<List<ContainerEntity>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromIngredientList(list: List<IngredientEntity>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toIngredientList(json: String): List<IngredientEntity> {
        val type = object : TypeToken<List<IngredientEntity>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromRecipeList(list: List<RecipeEntity>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toRecipeList(json: String): List<RecipeEntity> {
        val type = object : TypeToken<List<RecipeEntity>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromMemberList(members: List<Member>): String {
        return Gson().toJson(members)
    }

    @TypeConverter
    fun toMemberList(json: String): List<Member> {
        val type = object : TypeToken<List<Member>>() {}.type
        return Gson().fromJson(json, type)
    }
}