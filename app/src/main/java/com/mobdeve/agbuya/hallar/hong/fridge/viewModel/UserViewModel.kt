package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.agbuya.hallar.hong.fridge.repository.RecipeRepository
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserEntity
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import com.mobdeve.agbuya.hallar.hong.fridge.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// handles the connection between UserRepository + UserEntity
class UserViewModel(
    private val repository: UserRepository,
    private val containerRepo : ContainerRepository,
    private val recipeRepo : RecipeRepository
) : ViewModel() {

    private val _registrationResult = MutableStateFlow<Result<Long>?>(null)
    val registrationResult: StateFlow<Result<Long>?> = _registrationResult

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user
    val loggedInUser: StateFlow<UserEntity?> = _user.asStateFlow()

    // Save user to entity
    fun onLogin(userAuthId: String?, userDisplayName: String?) {
        viewModelScope.launch {
            if (userAuthId != null) {
                // First, check if user exists
                var user = repository.getUserByFirebaseId(userAuthId)

                // If user doesn't exist, create them
                if (user == null) {
                    user = UserEntity(
                        fireAuthId = userAuthId,
                        name = userDisplayName!!
                    )
                    // Insert the new user
                    repository.insertUser(user)
                    // Get the inserted user
                    user = repository.getUserByFirebaseId(userAuthId)
                }

                // Update the user in repositories
                repository.updateCurrentUserFireAuthId(userAuthId)
                repository.updateUserName(userDisplayName)
                recipeRepo.assignUserAuthId(userAuthId)
                containerRepo.assignFireAuthId(userAuthId)

                // Set the logged in user
                _user.value = user
            }
        }
    }

    fun onLogout() {
        //TODO
    }
}