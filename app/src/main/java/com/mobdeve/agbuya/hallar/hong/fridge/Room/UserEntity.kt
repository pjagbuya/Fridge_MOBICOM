package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.*

@Entity()
data class UserEntity(
    @PrimaryKey val id: Int = 0, //only stores 1 account at a time
    val name: String,
    val fireAuthId : String? = null

)
