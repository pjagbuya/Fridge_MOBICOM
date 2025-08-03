package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.agbuya.hallar.hong.fridge.repository.RecipeRepository
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserEntity
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import com.mobdeve.agbuya.hallar.hong.fridge.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// handles the connection between UserRepository + UserEntity
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val containerRepository: ContainerRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel()  {

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
                recipeRepository.assignUserAuthId(userAuthId)
                containerRepository.assignFireAuthId(userAuthId)

                // Set the logged in user
                _user.value = user
            }
        }
    }
    fun clearLoggedInUser() {
        viewModelScope.launch {
            _user.value = null // Clear the StateFlow
            // Potentially clear other related states if needed
        }
    }
    fun onLogout() {
        //TODO
    }
}