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
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ContainerSharedViewModel @Inject constructor(
    private val repository: ContainerRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    val containerIdNameMap = MutableStateFlow<List<ContainerIdName>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Get current user's Firebase UID
    private val currentFirebaseUid: String?
        get() = auth.currentUser?.uid

    // User-specific container data
    val readAllData: StateFlow<List<ContainerEntity>> =
        (currentFirebaseUid?.let { uid ->
            repository.getContainersByFirebaseUid(uid)
        } ?: emptyFlow())
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered data (user-specific) - only search filtering
    val filteredData: StateFlow<List<ContainerEntity>> = combine(
        readAllData,
        _searchQuery
    ) { data, search ->
        if (search.isNotBlank()) {
            data.filter { it.name.contains(search, ignoreCase = true) }
        } else {
            data
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
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

    fun getContainerIdNameMapDirect(): List<ContainerIdName> {
        return readAllData.value
            .map { ContainerIdName(it.containerId, it.name) }
    }

    fun fetchContainerIdNameMap() {
        viewModelScope.launch {
            currentFirebaseUid?.let { uid ->
                val result = repository.getContainerIdNameMapByFirebaseUid(uid)
                containerIdNameMap.value = result
            }
        }
    }

    // Return Flow instead of LiveData for API 23 compatibility
    fun searchDatabase(searchQuery: String): Flow<List<ContainerEntity>> {
        return currentFirebaseUid?.let { uid ->
            repository.searchDatabaseByFirebaseUid(searchQuery, uid)
        } ?: emptyFlow()
    }

    // Alternative: Manual LiveData creation
    fun searchDatabaseAsLiveData(searchQuery: String): LiveData<List<ContainerEntity>> {
        val liveData = MutableLiveData<List<ContainerEntity>>()

        viewModelScope.launch {
            currentFirebaseUid?.let { uid ->
                repository.searchDatabaseByFirebaseUid(searchQuery, uid).collect { result ->
                    liveData.postValue(result)
                }
            } ?: liveData.postValue(emptyList())
        }

        return liveData
    }
}