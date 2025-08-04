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



    fun syncUpdatedContainerCapacity(containerEntity: ContainerEntity, context: Context) {
        val TAG = "SYNC_CONT_CAP"


        try {
            val containerToUpdate = containerEntity

            if (containerToUpdate != null) {
                val firestoreHelper = FirestoreHelper(context)
                val firestoreContainer = containerToUpdate.toFirestoreContainer()
                firestoreHelper.syncToFirestore(
                    "containers",
                    containerToUpdate.containerId.toString(),
                    firestoreContainer,
                    "Container capacity synced",
                    "Failed to sync container capacity"
                )
            }
        } catch (e: Exception) {
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

    fun syncUpdateContainer(containerEntity: ContainerEntity, context: Context) {
        val TAG = "SYNC_CONT_UPDATE"

        // Basic validation - Ensure the entity has a valid ID
        if (containerEntity.containerId <= 0) {
            Log.w(TAG, "Cannot sync update for container with invalid ID: ${containerEntity.containerId}")
            return
        }

        viewModelScope.launch {
            delay(1000)
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

    fun addContainer(container: ContainerEntity, context: Context) {
        val TAG = "ADD_AND_SYNC_CONTAINER"

        // Basic validation
        if (container.name.isBlank()) {
            Log.w(TAG, "Cannot add container with blank name.")
            return
        }

        // Get current user ID for more specific search (if implemented in DAO/Repository)
        val currentUserUid = currentFirebaseUid // From your existing property

        viewModelScope.launch(Dispatchers.IO) { // Launch on IO for database operations
            try {
                Log.d(TAG, "Adding container to local database: ${container.name}")
                // 1. Add the container to the local Room database.
                repository.addContainer(container)
                Log.d(TAG, "Container '${container.name}' add request sent to local database.")

                // 2. Attempt to find the newly added container by name
                // Add a small delay to let Room process the insert

                Log.d(TAG, "Searching for newly added container by name: ${container.name}")

                // Find the container by name (user-specific search is better if implemented)
                val addedContainerEntity = if (currentUserUid != null) {
                    // Try user-specific search first (recommended)
                    // repository.findContainerByNameOnceForUser(container.name, currentUserUid)
                    // Fallback to simple name search for now or if user-specific isn't implemented
                    repository.findContainerByNameOnce(container.name) // Fallback
                } else {
                    repository.findContainerByNameOnce(container.name)
                }

                if (addedContainerEntity != null && addedContainerEntity.containerId > 0) {
                    Log.d(TAG, "Found newly added container entity with ID: ${addedContainerEntity.containerId}")
                    // 3. Trigger sync with the entity retrieved from the database
                    // This ensures the sync function gets the correct, database-assigned containerId
                    syncAddContainer(addedContainerEntity, context) // Use the direct entity sync function
                } else {
                    Log.w(TAG, "Could not find the newly added container '${container.name}' in the database after insert.")
                    // Optional: Handle case where entity isn't found
                    // Maybe retry the find operation or log an error
                }

            } catch (e: CancellationException) {
                // Handle coroutine cancellation (e.g., if ViewModel is cleared)
                Log.i(TAG, "addContainer coroutine was cancelled for: ${container.name}. ViewModel might be cleared.", e)
                // Optional: Schedule WorkManager task for guaranteed sync if critical
            } catch (e: Exception) {
                Log.e(TAG, "Error adding or syncing container: ${container.name}", e)
                // Handle error (e.g., show user message via LiveData, retry mechanism)
            }
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

    fun increaseCurrCap(containerId: Int, context: Context) {
        val TAG = "INCREASE_CAP"
        if (containerId <= 0) {
            Log.w(TAG, "Invalid container ID for increase: $containerId")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "Increasing capacity for container ID: $containerId")
                // 1. Update the local database
                repository.increaseCurrCap(containerId)
                Log.d(TAG, "Local capacity increased for container ID: $containerId")

                // 2. Find the updated container entity and sync it
                // Add a small delay to let Room process the update
                delay(200)
                val updatedContainerEntity = findContainerById(containerId)
                if (updatedContainerEntity != null) {
                    Log.d(TAG, "Found updated container, syncing capacity. New currCap: ${updatedContainerEntity.currCap}")
                    syncUpdatedContainerCapacity(updatedContainerEntity, context)
                } else {
                    Log.w(TAG, "Could not find container ID $containerId to sync after increase.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error increasing capacity or syncing for container ID: $containerId", e)
            }
        }
    }

    fun decreaseCurrCap(containerId: Int, context: Context) {
        val TAG = "DECREASE_CAP"
        if (containerId <= 0) {
            Log.w(TAG, "Invalid container ID for decrease: $containerId")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "Decreasing capacity for container ID: $containerId")
                // 1. Update the local database
                repository.decreaseCurrCap(containerId)
                Log.d(TAG, "Local capacity decreased for container ID: $containerId")

                // 2. Find the updated container entity and sync it
                // Add a small delay to let Room process the update
                val updatedContainerEntity = findContainerById(containerId) // <-- Uses findContainerById
                if (updatedContainerEntity != null) {
                    Log.d(TAG, "Found updated container, syncing capacity. New currCap: ${updatedContainerEntity.currCap}")
                    // Call the suspend version directly within this coroutine
                    syncUpdatedContainerCapacity(updatedContainerEntity, context) // <-- Pass the found entity
                } else {
                    Log.w(TAG, "Could not find container ID $containerId to sync after decrease.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error decreasing capacity or syncing for container ID: $containerId", e)
            }
        }
    }
    private suspend fun findContainerById(containerId: Int): ContainerEntity? {
        val TAG = "FIND_CONT_BY_ID"
        return try {
            // Get the current list of containers from the StateFlow
            val currentContainers = readAllData.value
            // Find the one with the matching ID
            val foundContainer = currentContainers.find { it.containerId == containerId }
            Log.d(TAG, "Container ID $containerId ${if (foundContainer != null) "found" else "not found"} in readAllData.value (${currentContainers.size} items)")
            foundContainer
        } catch (e: Exception) {
            Log.e(TAG, "Error finding container by ID: $containerId", e)
            null
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