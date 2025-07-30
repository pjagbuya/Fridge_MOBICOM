package com.mobdeve.agbuya.hallar.hong.fridge.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobdeve.agbuya.hallar.hong.fridge.repository.UserRepository
import com.mobdeve.agbuya.hallar.hong.fridge.viewmodels.UserViewModel

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

