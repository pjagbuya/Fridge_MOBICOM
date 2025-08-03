package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "RecipeEntity"
)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val inventoryId : String? = "inv_sample_001", // 0 for own,
    val userId : Int = 1,
    val userAuthId : String? = null, //user's fireAuthId if logged in
    val name: String,
    val description: String = ""
)
