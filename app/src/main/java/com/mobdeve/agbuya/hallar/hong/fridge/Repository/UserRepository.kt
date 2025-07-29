package com.mobdeve.agbuya.hallar.hong.fridge.Room

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    // insert user with error handling for duplicate email
    suspend fun registerUser(user: UserEntity): Result<Long> {
        return try {
            withContext(Dispatchers.IO) {
                val id = userDao.insertUser(user)
                Log.d("UserRepository", "Inserted user: ${user.email}") // for debugging
                Result.success(id)
            }
        } catch (e: SQLiteConstraintException) {
            Log.d("UserRepository", "Failed to insert user: ${user.email}, email already exists.") // for debugging
            Result.failure(Exception("Email already exists."))
        }
    }

    // get user by email
    suspend fun getUserByEmail(email: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByEmail(email)
        }
    }

    // get user by ID
    suspend fun getUserById(id: Int): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.getUserById(id)
        }
    }

    // get a user by name
    suspend fun getUserByName(name: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByName(name)
        }
    }

    // get all users
    suspend fun getAllUsers(): List<UserEntity> {
        return withContext(Dispatchers.IO) {
            userDao.getAllUsers()
        }
    }

//    // update user
//    suspend fun updateUser(user: UserEntity) {
//        withContext(Dispatchers.IO) {
//            userDao.updateUser(user)
//        }
//    }
//
//    // delete user
//    suspend fun deleteUser(user: UserEntity) {
//        withContext(Dispatchers.IO) {
//            userDao.deleteUser(user)
//        }
//    }
}
