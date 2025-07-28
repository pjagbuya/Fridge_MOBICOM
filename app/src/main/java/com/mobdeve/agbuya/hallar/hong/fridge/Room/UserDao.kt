package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    // get users by ID
    @Query("SELECT * FROM UserEntity WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?

    // get users by name
    @Query("SELECT * FROM UserEntity WHERE name = :name")
    suspend fun getUserByName(name: String): UserEntity?

    // get users by email
    @Query("SELECT * FROM UserEntity WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    // get all users
    @Query("SELECT * FROM UserEntity")
    suspend fun getAllUsers(): List<UserEntity>
}
