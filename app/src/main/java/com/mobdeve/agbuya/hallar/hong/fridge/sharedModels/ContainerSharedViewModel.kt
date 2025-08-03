package com.mobdeve.agbuya.hallar.hong.fridge.sharedModels


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.agbuya.hallar.hong.fridge.dao.ContainerDao
import com.mobdeve.agbuya.hallar.hong.fridge.dao.ContainerIdName
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContainerSharedViewModel @Inject constructor(
    private val repository: ContainerRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    val containerIdNameMap = MutableStateFlow<List<ContainerIdName>>(emptyList())

    private val _readAllData = MutableStateFlow<List<ContainerEntity>>(emptyList())
    val readAllData: StateFlow<List<ContainerEntity>> = _readAllData

    init {
        viewModelScope.launch {
            repository.readAllData.collect {
                _readAllData.value = it
            }
        }
    }

    fun addContainer(container: ContainerEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addContainer(container)
        }
    }

    fun updateContainer(container: ContainerEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateContainer(container)
        }
    }

    fun deleteContainer(containerId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteContainer(containerId)
        }
    }

    fun decreaseCurrCap(containerId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.decreaseCurrCap(containerId)
        }
    }

    fun getContainerIdNameMapDirect(userId: Int): List<ContainerIdName> {
        return _readAllData.value
            .filter { it.ownerUserId == userId }
            .map { ContainerIdName(it.containerId, it.name) }
    }

    fun fetchContainerIdNameMap(userId: Int) {
        viewModelScope.launch {
            val result = repository.getContainerIdNameMap(userId)
            containerIdNameMap.value = result
        }
    }
}
