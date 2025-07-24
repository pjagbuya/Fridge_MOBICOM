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
    @Query("SELECT * FROM USERS WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?

    // get users by name
    @Query("SELECT * FROM USERS WHERE name = :name")
    suspend fun getUserByName(name: String): UserEntity?

    // get users by email
    @Query("SELECT * FROM USERS WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    // get all users
    @Query("SELECT * FROM USERS")
    suspend fun getAllUsers(): List<UserEntity>
}
