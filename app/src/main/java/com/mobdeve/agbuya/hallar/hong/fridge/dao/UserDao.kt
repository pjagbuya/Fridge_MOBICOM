package com.mobdeve.agbuya.hallar.hong.fridge.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long


    @Query("SELECT * FROM UserEntity LIMIT 1")
    suspend fun getFirstUser(): UserEntity?


    @Query("SELECT * FROM UserEntity")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("DELETE FROM UserEntity")
    suspend fun deleteAll()
}