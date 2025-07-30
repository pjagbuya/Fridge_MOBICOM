package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import com.mobdeve.agbuya.hallar.hong.fridge.Room.ContainerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//import okhttp3.Dispatcher

class ContainerSharedViewModel(application : Application): AndroidViewModel(application){
    val readAllData : LiveData<List<ContainerEntity>>
    private val repository: ContainerRepository

    init {
        val containerDao = AppDatabase.getInstance(application).containerDao()
        repository = ContainerRepository(containerDao)
        readAllData = repository.readAllData
    }

    fun addContainer(container : ContainerEntity){
        // viewModel Scope a coroutine, Dispatchers.IO sets it as background process
        viewModelScope.launch(Dispatchers.IO) {
            repository.addContainer(container)
        }
    }

    fun updateContainer(container : ContainerEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateContainer(container)
        }
    }

}