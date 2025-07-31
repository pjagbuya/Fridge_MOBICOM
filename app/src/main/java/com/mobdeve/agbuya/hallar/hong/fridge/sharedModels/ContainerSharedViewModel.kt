package com.mobdeve.agbuya.hallar.hong.fridge.sharedModels

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerDataHelper
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.dao.ContainerIdName
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class ContainerSharedViewModel(application : Application): AndroidViewModel(application){
    val readAllData : LiveData<List<ContainerEntity>>
    private val repository: ContainerRepository
    val containerIdNameMap = MutableLiveData<List<ContainerIdName>>() // Live observable result



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

    fun deleteContainer(containerId : Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteContainer(containerId)
        }
    }
    fun decreaseCurrCap(containerId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.decreaseCurrCap(containerId)
        }
    }

    fun getContainerIdNameMapDirect(userId: Int): List<ContainerIdName> {
        return readAllData.value
            ?.filter { it.ownerUserId == userId }
            ?.map { ContainerIdName(it.containerId, it.name) }
            ?: emptyList()
    }

    fun fetchContainerIdNameMap(userId: Int) {
        viewModelScope.launch {
            val result = repository.getContainerIdNameMap(userId)
            containerIdNameMap.postValue(result)
        }
    }

}