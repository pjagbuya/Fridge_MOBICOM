package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobdeve.agbuya.hallar.hong.fridge.repository.RecipeRepository
import com.mobdeve.agbuya.hallar.hong.fridge.repository.UserRepository


class SharedRecipeViewModelFactory(
    private val userRepository: UserRepository,
    private val recipeRepository: RecipeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedRecipeViewModel::class.java)) {
            return SharedRecipeViewModel(userRepository, recipeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}