package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.*

@Entity(indices = [Index(value = ["email"], unique = true)])

data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
)
