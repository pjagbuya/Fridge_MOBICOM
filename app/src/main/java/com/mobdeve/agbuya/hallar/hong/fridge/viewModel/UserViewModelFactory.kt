package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import com.mobdeve.agbuya.hallar.hong.fridge.Repository.RecipeRepository
import com.mobdeve.agbuya.hallar.hong.fridge.repository.UserRepository

class UserViewModelFactory(private val repository: UserRepository,
                           private val recipeRepo: RecipeRepository,
                           private val containerRepo: ContainerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository, containerRepo, recipeRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
