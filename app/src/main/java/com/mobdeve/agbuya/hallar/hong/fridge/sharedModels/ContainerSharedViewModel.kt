package com.mobdeve.agbuya.hallar.hong.fridge.sharedModels


import android.app.Application
import android.content.Context
import android.util.Log
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
import com.mobdeve.agbuya.hallar.hong.fridge.converters.toFirestoreContainer
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreHelper.FirestoreHelper
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    fun syncUpdatedContainerCapacity(containerId: Int, context: Context) {
        val TAG = "SYNC_CONT_CAP"
        if (containerId <= 0) {
            Log.w(TAG, "Invalid container ID for sync: $containerId")
            return
        }

        viewModelScope.launch {
            try {
                delay(2000) // Give Room/Flow time to update
                val currentContainers = readAllData.value // Get user-specific containers
                val containerToUpdate = currentContainers.find { it.containerId == containerId }

                if (containerToUpdate != null) {
                    Log.d(TAG, "Syncing updated capacity for container ID: $containerId")
                    val firestoreHelper = FirestoreHelper(context)
                    val firestoreContainer = containerToUpdate.toFirestoreContainer()
                    firestoreHelper.syncToFirestore(
                        "containers",
                        containerToUpdate.containerId.toString(),
                        firestoreContainer,
                        "Container capacity synced",
                        "Failed to sync container capacity"
                    )
                } else {
                    Log.w(TAG, "Container ID $containerId not found for sync.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error syncing container capacity for ID: $containerId", e)
            }
        }
    }
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
    fun increaseCurrCap(containerId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.increaseCurrCap(containerId)
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