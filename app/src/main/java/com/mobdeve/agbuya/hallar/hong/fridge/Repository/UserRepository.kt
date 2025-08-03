package com.mobdeve.agbuya.hallar.hong.fridge.Room

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    // insert user with error handling for duplicate user
    suspend fun registerUser(user: UserEntity): Result<Long> {
        return try {
            withContext(Dispatchers.IO) {
                val id = userDao.insertUser(user)
                Log.d("UserRepository", "Inserted user: ${user.name}") // for debugging
                Result.success(id)
            }
        } catch (e: SQLiteConstraintException) {
            Log.d("UserRepository", "Failed to insert user: ${user.name}") // for debugging
            Result.failure(Exception("user already exists"))
        }
    }

    //get user
    suspend fun getUser() : Flow<UserEntity?> {
        return withContext(Dispatchers.IO) {
            userDao.getUser()
        }
    }

    //update fireAuthId
    suspend fun updateCurrentUserFireAuthId(fireAuthId: String?) {
        userDao.updateFireAuthId(fireAuthId)
    }



    // get user by fireAuth
    suspend fun getUserByAuthId(userAuthId: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByAuthId(userAuthId)
        }
    }

    suspend fun getMembers(userAuthId: String): MemberEntity? {
        return withContext(context = Dispatchers.IO){
            userDao.getMembers(userAuthId)
        }
    }

    suspend fun getLoggedInUser(userAuthId: String?): UserEntity? {
        return withContext(context = Dispatchers.IO){
            userDao.getLoggedInUser(userAuthId)
        }
    }

    suspend fun updateUserName(name: String?) {
        return userDao.updateName(name)
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
