package com.mobdeve.agbuya.hallar.hong.fridge.sharedModels


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.agbuya.hallar.hong.fridge.dao.ContainerDao
import com.mobdeve.agbuya.hallar.hong.fridge.dao.ContainerIdName
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedSortBy = MutableStateFlow("A-Z")
    val selectedSortBy: StateFlow<String> = _selectedSortBy

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    fun setSort(sort: String) {
        _selectedSortBy.value = sort
    }
    val filteredData: StateFlow<List<ContainerEntity>> =
        combine(readAllData, _searchQuery) { data, search ->
            if (search.isNotBlank()) {
                data.filter { it.name.contains(search, ignoreCase = true) }
            } else {
                data
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
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

    fun searchDatabase(searchQuery:String): LiveData<List<ContainerEntity>>{
        return repository.searchDatabase(searchQuery).asLiveData()
    }
}
