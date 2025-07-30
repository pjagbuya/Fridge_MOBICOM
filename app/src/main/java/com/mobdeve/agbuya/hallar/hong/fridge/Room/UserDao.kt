package com.mobdeve.agbuya.hallar.hong.fridge.dao

import androidx.room.*
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE firebaseUid = :firebaseUid LIMIT 1")
    suspend fun getUserByFirebaseUid(firebaseUid: String): UserEntity?


}
