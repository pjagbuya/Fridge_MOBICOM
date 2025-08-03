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
import com.google.firebase.firestore.FirebaseFirestore
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
import kotlin.coroutines.cancellation.CancellationException

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
    // Inside ContainerSharedViewModel.kt

    fun syncAddContainer(containerEntity: ContainerEntity, context: Context) {
        val TAG = "SYNC_CONT_ADD"

        // Basic validation - Ensure the entity has a valid ID
        // This is crucial for sync operations.
        if (containerEntity.containerId <= 0) {
            Log.w(TAG, "Cannot sync newly added container with invalid ID: ${containerEntity.containerId}")
            // Consider how to handle this. If called immediately after addContainer,
            // the ID might not be set yet in the passed object.
            // Options:
            // 1. Return early (current approach).
            // 2. Add a delay and re-fetch from readAllData (like syncNewlyAddedIngredient).
            // 3. Modify addContainer to return the new ID or entity (most robust).
            return
        }

        viewModelScope.launch {
            Log.d(TAG, "Starting sync add for container ID: ${containerEntity.containerId}")

            // A delay might be needed to ensure Room has definitely finished assigning the ID
            // and any related transactions (like updating user/container links) are complete.
            // However, it doesn't need to be as long as waiting for Flow emission.

            try {
                Log.d(TAG, "Creating FirestoreHelper and converting container ID: ${containerEntity.containerId}")
                val firestoreHelper = FirestoreHelper(context)
                val firestoreContainer = containerEntity.toFirestoreContainer() // Convert to Firestore model
                val documentId = containerEntity.containerId.toString() // Use container ID as Firestore Doc ID

                Log.d(TAG, "Syncing newly added container data for Doc ID: $documentId, Name: '${containerEntity.name}'")
                // Sync the new container data (creates/overwrites the document in Firestore)
                firestoreHelper.syncToFirestore(
                    collectionName = "containers", // Make sure this matches your intended Firestore collection
                    documentId = documentId,       // Document ID is the container's ID
                    data = firestoreContainer,     // Data to sync
                    successMessage = "Container '${containerEntity.name}' added and synced successfully",
                    failureMessage = "Failed to sync added container '${containerEntity.name}'"
                )
                Log.d(TAG, "Finished sync call for newly added container ID: ${containerEntity.containerId}")

            } catch (e: CancellationException) {
                // viewModelScope job was cancelled
                Log.i(TAG, "Sync add for container ID: ${containerEntity.containerId} was cancelled.", e)
            } catch (e: Exception) {
                // Handle general errors during the sync process
                Log.e(TAG, "Error during syncAddContainer for container ID: ${containerEntity.containerId}", e)
                // Optional: Implement retry logic or notify user via LiveData if needed
            } finally {
                Log.d(TAG, "syncAddContainer coroutine finished for container ID: ${containerEntity.containerId}")
            }
        }
        Log.d(TAG, "syncAddContainer function launched viewModelScope coroutine for container ID: ${containerEntity.containerId}")
    }

    fun syncUpdateContainer(containerEntity: ContainerEntity, context: Context) {
        val TAG = "SYNC_CONT_UPDATE"

        // Basic validation - Ensure the entity has a valid ID
        if (containerEntity.containerId <= 0) {
            Log.w(TAG, "Cannot sync update for container with invalid ID: ${containerEntity.containerId}")
            return
        }

        viewModelScope.launch {
            Log.d(TAG, "Starting sync update for container ID: ${containerEntity.containerId}")
            // A small delay might be useful to ensure Room transaction is fully complete,
            // but it doesn't need to be as long as waiting for Flow emission like in 'add' scenarios.

            try {
                Log.d(TAG, "Creating FirestoreHelper and converting container ID: ${containerEntity.containerId}")
                val firestoreHelper = FirestoreHelper(context)
                val firestoreContainer = containerEntity.toFirestoreContainer()
                val documentId = containerEntity.containerId.toString() // Use container ID as document ID

                Log.d(TAG, "Syncing updated container data for Doc ID: $documentId, Name: '${containerEntity.name}'")
                // Sync the updated container data (overwrites the existing document)
                firestoreHelper.syncToFirestore(
                    collectionName = "containers", // Make sure this matches your intended Firestore collection
                    documentId = documentId,
                    data = firestoreContainer,
                    successMessage = "Container '${containerEntity.name}' update synced successfully",
                    failureMessage = "Failed to sync updated container '${containerEntity.name}'"
                )
                Log.d(TAG, "Finished sync call for updated container ID: ${containerEntity.containerId}")

            } catch (e: CancellationException) {
                // viewModelScope job was cancelled
                Log.i(TAG, "Sync update for container ID: ${containerEntity.containerId} was cancelled.", e)
            } catch (e: Exception) {
                // Handle general errors during the sync process
                Log.e(TAG, "Error during syncUpdateContainer for container ID: ${containerEntity.containerId}", e)
                // Optional: Implement retry logic or notify user via LiveData if needed
            } finally {
                Log.d(TAG, "syncUpdateContainer coroutine finished for container ID: ${containerEntity.containerId}")
            }
        }
        Log.d(TAG, "syncUpdateContainer function launched viewModelScope coroutine for container ID: ${containerEntity.containerId}")
    }
    fun syncDeleteContainer(containerId: Int) {
        val TAG = "SYNC_CONT_DELETE"

        if (containerId <= 0) {
            Log.w(TAG, "Cannot sync delete for invalid container ID: $containerId")
            return
        }

        val documentId = containerId.toString()

        viewModelScope.launch {
            Log.d(TAG, "Starting sync delete for container ID: $containerId (Doc ID: $documentId)")
            Log.d(TAG, "Delay finished, attempting Firestore delete for Doc ID: $documentId")

            try {
                // Perform the deletion directly on Firestore
                // Uses the document ID (which is the containerId) to identify the document.
                FirebaseFirestore.getInstance()
                    .collection("containers") // Make sure "containers" is your intended Firestore collection name
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "Successfully deleted container document: $documentId from Firestore")
                        // Deletion successful in Firestore
                        // Optional: Trigger any UI updates if needed (though container is deleted)
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Failed to delete container document: $documentId from Firestore", exception)
                        // Deletion failed in Firestore
                        // Optional: Implement retry logic or notify user
                    }

            } catch (e: Exception) {
                // Catch unexpected errors during setup/launch
                Log.e(TAG, "Error initiating delete sync for container ID: $containerId", e)
                // Optional: Handle general errors
            } finally {
                Log.d(TAG, "Sync delete attempt finished for container ID: $containerId")
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