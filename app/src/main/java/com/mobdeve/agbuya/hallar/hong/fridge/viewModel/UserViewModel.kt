package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// handles the connection between UserRepository + UserEntity
class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _registrationResult = MutableStateFlow<Result<Long>?>(null)
    val registrationResult: StateFlow<Result<Long>?> = _registrationResult

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    // register user
    fun registerUser(user: UserEntity) {
        viewModelScope.launch {
            val existingUser = repository.getUserByEmail(user.email)
            if (existingUser != null) {
                _registrationResult.value = Result.failure(Exception("Email already exists"))
            } else {
                val result = repository.registerUser(user)
                _registrationResult.value = result

                // for debugging check
                if (result.isSuccess) {
                    val id = result.getOrNull()
                    Log.d("SIGNUP_SUCCESS", "User inserted with ID: $id, email: ${user.email}")
                }
            }
        }
    }

    // login user by email and password
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val foundUser = repository.getUserByEmail(email)
            if (foundUser != null && foundUser.password == password) {
                _user.value = foundUser
            } else {
                _user.value = null // user invalid login
            }
        }
    }

    fun logoutUser() {
        _user.value = null
    }
}
