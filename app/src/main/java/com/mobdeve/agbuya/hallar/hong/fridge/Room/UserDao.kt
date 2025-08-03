package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

//    @Update
//    suspend fun updateUser(user: String)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    // get users by ID
    @Query("SELECT * FROM UserEntity WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?

    @Query("SELECT * FROM UserEntity WHERE id = :userAuthId")
    suspend fun getUserByAuthId(userAuthId: String): UserEntity?

    // get users by name
    @Query("SELECT * FROM UserEntity WHERE name = :name")
    suspend fun getUserByName(name: String): UserEntity?

    @Query("SELECT * FROM UserEntity WHERE id = 0 LIMIT 1") //gets the current user
    fun getUser(): Flow<UserEntity?>

    //update fireAuthId
    @Query("UPDATE UserEntity SET fireAuthId = :fireAuthId WHERE id = 0")
    suspend fun updateFireAuthId(fireAuthId: String?) : Int

    @Query("UPDATE UserEntity SET name = :name WHERE name = 'DEFAULT USER'")
    suspend fun updateName(name: String?)

    // get users by email
//    @Query("SELECT * FROM UserEntity WHERE email = :email LIMIT 1")
//    suspend fun getUserByEmail(email: String): UserEntity?


    @Query("SELECT * FROM MemberEntity WHERE fireAuthId = :id")
    suspend fun getMembers(id: String): MemberEntity

    @Query("SELECT * FROM UserEntity WHERE fireAuthId = :id")
    suspend fun getLoggedInUser(id: String?): UserEntity

    // get all users
    @Query("SELECT * FROM UserEntity")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("DELETE FROM UserEntity")
    suspend fun deleteAll()
    @Query("SELECT * FROM UserEntity WHERE fireAuthId = :firebaseId LIMIT 1")
    suspend fun getUserByFirebaseId(firebaseId: String): UserEntity?


}
