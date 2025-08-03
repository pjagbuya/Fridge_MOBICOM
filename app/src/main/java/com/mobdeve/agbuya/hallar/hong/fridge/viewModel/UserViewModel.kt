package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.agbuya.hallar.hong.fridge.Repository.RecipeRepository
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserRepository
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// handles the connection between UserRepository + UserEntity
class UserViewModel(
    private val repository: UserRepository,
    private val containerRepo : ContainerRepository,
    private val recipeRepo : RecipeRepository,

    ) : ViewModel() {

    private val _registrationResult = MutableStateFlow<Result<Long>?>(null)
    val registrationResult: StateFlow<Result<Long>?> = _registrationResult

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user
    val loggedInUser: StateFlow<UserEntity?> = _user.asStateFlow()

    //save user to entity
    fun onLogin(userAuthId: String?, userDisplayName : String?){
        viewModelScope.launch {
            //save the current user's auth id to the containers and stuff
            repository.updateCurrentUserFireAuthId(userAuthId)
            repository.updateUserName(userDisplayName)
            recipeRepo.assignUserAuthId(userAuthId)
            containerRepo.assignFireAuthId(userAuthId)
            val user = repository.getLoggedInUser(userAuthId)
            _user.value = user
        }
    }

//    fun getLoggedInUser() : MutableStateFlow<UserEntity?> {
//        return _user
//    }

    fun onLogout() {
        //TODO
    }





    // register user
//    fun registerUser(user: UserEntity) {
//        viewModelScope.launch {
//            val existingUser = repository.getUserBy (user.email)
//            if (existingUser != null) {
//                _registrationResult.value = Result.failure(Exception("Email already exists"))
//            } else {
//                val result = repository.registerUser(user)
//                _registrationResult.value = result
//
//                // for debugging check
//                if (result.isSuccess) {
//                    val id = result.getOrNull()
//                    Log.d("SIGNUP_SUCCESS", "User inserted with ID: $id, email: ${user.email}")
//                }
//            }
//        }
//    }

    // login user by email and password
//    fun loginUser(email: String, password: String) {
//        viewModelScope.launch {
//            val foundUser = repository.getUserByEmail(email)
//            if (foundUser != null && foundUser.password == password) {
//                _user.value = foundUser
//            } else {
//                _user.value = null // user invalid login
//            }
//        }
//    }

//    fun logoutUser() {
//        _user.value = null
//    }
}
