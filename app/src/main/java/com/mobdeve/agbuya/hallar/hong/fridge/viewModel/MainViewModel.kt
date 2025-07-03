package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.repository.MainRepository

class MainViewModel:ViewModel() {
    private val repository = MainRepository()

    fun loadContainer():LiveData<MutableList<ContainerModel>> {
        return repository.loadContainer()
    }
}