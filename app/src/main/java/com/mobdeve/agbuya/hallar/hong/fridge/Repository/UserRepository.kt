package com.mobdeve.agbuya.hallar.hong.fridge.repository

import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserEntity
import com.mobdeve.agbuya.hallar.hong.fridge.dao.UserDao

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUserByFirebaseUid(firebaseUid: String): UserEntity? {
        return userDao.getUserByFirebaseUid(firebaseUid)
    }
}
