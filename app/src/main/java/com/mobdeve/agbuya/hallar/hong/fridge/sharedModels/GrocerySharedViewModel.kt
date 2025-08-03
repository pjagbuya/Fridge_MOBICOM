package com.mobdeve.agbuya.hallar.hong.fridge.sharedModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserDao
import com.mobdeve.agbuya.hallar.hong.fridge.converters.toFirestoreIngredient
import com.mobdeve.agbuya.hallar.hong.fridge.dao.IngredientDao
import kotlinx.coroutines.launch
import com.mobdeve.agbuya.hallar.hong.fridge.extensions.toDomainModel
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreHelper.FirestoreHelper
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import com.mobdeve.agbuya.hallar.hong.fridge.repository.GroceryRepository
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class GrocerySharedViewModel @Inject constructor(
    private val repository: GroceryRepository,
    private val auth: FirebaseAuth // Remove unused UserDao
) : ViewModel() {

    private val _selectedSortBy = MutableStateFlow("")
    private val _selectedItemType = MutableStateFlow("All items")
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val currentFirebaseUid: String?
        get() = auth.currentUser?.uid

    // Updated readAllData - filtered by current user
    val readAllData: StateFlow<List<IngredientEntity>> =
        (currentFirebaseUid?.let { uid ->
            repository.getIngredientsByFirebaseUid(uid)
        } ?: emptyFlow())
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Updated filteredAndSortedData
    val filteredAndSortedData: StateFlow<List<IngredientEntity>> = combine(
        readAllData,
        _selectedSortBy,
        _selectedItemType,
        _searchQuery
    ) { data, sortBy, itemType, search ->

        val filteredByType = if (itemType != "All items") {
            data.filter { it.itemType.equals(itemType, ignoreCase = true) }
        } else data

        val filteredBySearch = if (search.isNotBlank()) {
            filteredByType.filter {
                it.name.contains(search, ignoreCase = true) ||
                        it.itemType.contains(search, ignoreCase = true)
            }
        } else filteredByType

        when (sortBy) {
            "A-Z" -> filteredBySearch.sortedBy { it.name.lowercase() }
            "Quantity ↑" -> filteredBySearch.sortedBy { it.quantity }
            "Quantity ↓" -> filteredBySearch.sortedByDescending { it.quantity }
            "Date Added" -> filteredBySearch.sortedByDescending { it.dateAdded }
            "Expiry" -> filteredBySearch.sortedBy { it.expirationDate }
            else -> filteredBySearch
        }

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    init {
        viewModelScope.launch {
            Log.d("GROCERY_VM", "ViewModel initialized")
            Log.d("GROCERY_VM", "Current Firebase UID: $currentFirebaseUid")

            readAllData.collect { ingredients ->
                Log.d("GROCERY_VM", "Emitted ${ingredients.size} ingredients")
                ingredients.forEachIndexed { index, ingredient ->
                    Log.d("GROCERY_VM", "[$index] ID: ${ingredient.ingredientID}, Name: ${ingredient.name}")
                }
            }
        }
    }

    fun syncUpdatedIngredient(ingredientEntity: IngredientEntity, context: Context) {
        // Basic validation - Ensure the entity has a valid ID
        if (ingredientEntity.ingredientID <= 0) {
            return // Silently ignore items without a valid ID
        }

        val firestoreHelper = FirestoreHelper(context)

        viewModelScope.launch {

            try {
                val firestoreIngredient = ingredientEntity.toFirestoreIngredient()
                val documentId = ingredientEntity.ingredientID.toString()

                firestoreHelper.syncToFirestore(
                    collectionName = "ingredients",
                    documentId = documentId,
                    data = firestoreIngredient,
                    successMessage = "Ingredient '${ingredientEntity.name}' update synced successfully",
                    failureMessage = "Failed to sync updated ingredient '${ingredientEntity.name}'"
                )

            } catch (e: Exception) {
                // Catch unexpected errors during processing (no log)
            }
        }
    }

    fun syncDeletedIngredient(ingredientId: Int, context : Context) {
        val TAG = "SYNC_DELETE"

        if (ingredientId <= 0) {
            Log.w(TAG, "Cannot sync delete for invalid ingredient ID: $ingredientId")
            return
        }

        val documentId = ingredientId.toString()

        viewModelScope.launch {
            Log.d(TAG, "Starting sync delete for ingredient ID: $ingredientId (Doc ID: $documentId)")
            Log.d(TAG, "Delay finished, attempting Firestore delete for Doc ID: $documentId")

            try {
                val firestoreHelper = FirestoreHelper(context) // You'd need context passed in or instantiated differently
                // OR, if you instantiate FirestoreHelper elsewhere and pass it in...
                firestoreHelper.deleteFromFirestore(
                    collectionName = "ingredients",
                    documentId = documentId,
                    successMessage = "Ingredient deleted from cloud", // Customize as needed
                    failureMessage = "Failed to delete ingredient from cloud" // Customize as needed
                )
                // Note: The Toast messages are handled inside deleteFromFirestore based on the example above.

            } catch (e: Exception) {
                // Catch unexpected errors during setup/launch
                Log.e(TAG, "Error initiating delete sync for ingredient ID: $ingredientId", e)
                // Optionally show a general error Toast here if not handled in deleteFromFirestore
                // Toast.makeText(context, "Sync error for delete", Toast.LENGTH_SHORT).show()
            } finally {
                Log.d(TAG, "Sync delete attempt finished for ingredient ID: $ingredientId")
            }
        }
    }
    fun syncNewlyAddedIngredient(ingredientEntity: IngredientEntity, context: Context) {
        // Basic validation - Ensure the entity has a valid ID
        if (ingredientEntity.ingredientID <= 0) {
            return // Silently ignore items without a valid ID
        }

        val firestoreHelper = FirestoreHelper(context)

        viewModelScope.launch {
            delay(5000)
            val firestoreIngredient = ingredientEntity.toFirestoreIngredient()
            val documentId = ingredientEntity.ingredientID.toString()

            firestoreHelper.syncToFirestore(
                collectionName = "ingredients",
                documentId = documentId,
                data = firestoreIngredient,
                successMessage = "Ingredient '${ingredientEntity.name}' synced successfully",
                failureMessage = "Failed to sync ingredient '${ingredientEntity.name}'"
            )

        }
    }


    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSort(sort: String) {
        _selectedSortBy.value = sort
    }

    fun setItemTypeFilter(itemType: String) {
        _selectedItemType.value = itemType
    }

    fun findGrocery(id: Int): Flow<IngredientEntity> {
        return repository.findGrocery(id)
    }

    fun addGrocery(grocery: IngredientEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addGrocery(grocery)
        }
    }

    fun deleteGrocery(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteGrocery(id)
        }
    }

    fun deleteAllGroceryAtContainer(id: Int, context: Context) {
        val TAG = "DELETE_ALL_ING"

        if (id <= 0) {
            Log.w(TAG, "Invalid container ID provided for delete all: $id")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "Starting local delete for all groceries in container ID: $id")
                // 1. Perform the local database deletion first
                repository.deleteAllGroceryAtContainer(id)
                Log.d(TAG, "Local delete completed for container ID: $id")

                // 2. Attempt to sync deletions to Firestore
                // Option A: Use readAllData Flow and filter (if no specific DAO method)
                Log.d(TAG, "Fetching ingredients for container ID: $id from readAllData for Firestore sync")
                // Note: collectLatest might not be ideal here as we want the current list once.
                // Using first() or firstOrNull() from the Flow is better for a one-time read.
                // However, readAllData might be user-filtered. Ensure it includes items for container 'id'.
                // If readAllData is filtered by user, and the container belongs to this user,
                // then this should work. Otherwise, a direct DAO query is better.
                val currentIngredients = readAllData.value // Get current value
                val ingredientsToDelete = currentIngredients.filter { it.attachedContainerId == id }
                Log.d(TAG, "Found ${ingredientsToDelete.size} ingredients to delete from Firestore for container ID: $id")

                // Launch separate coroutines for each delete to avoid blocking this IO thread
                // and to allow parallel processing (if Firestore allows/it's efficient).
                // Using SupervisorJob so one failure doesn't cancel others.
                val syncDeleteScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
                for (ingredient in ingredientsToDelete) {
                    // Launch each delete sync in the shared viewModelScope or a sub-scope
                    // Using viewModelScope.launch ensures it's tied to ViewModel lifecycle.
                    // Delay might still be needed inside syncDeletedIngredient.
                    // Consider if launching many delayed coroutines is ideal.
                    // Alternative: Call syncDeletedIngredient without delay here, or use WorkManager.
                    viewModelScope.launch {
                        Log.d(TAG, "Scheduling Firestore delete for ingredient ID: ${ingredient.ingredientID}")
                        // Call the existing syncDeletedIngredient function for each item
                        // It handles its own delay and logging (assuming it uses FirestoreHelper)
                        // Pass context if needed, or modify syncDeletedIngredient to not require it
                        // if it uses FirebaseFirestore.getInstance() directly.
                        syncDeletedIngredient(ingredient.ingredientID, context) // Use existing function
                        // If syncDeletedIngredient requires context:
                        // syncDeletedIngredient(ingredient.ingredientID, getApplication<Application>().applicationContext)
                    }
                }
                // syncDeleteScope.coroutineContext.cancel() // Not needed if using viewModelScope.launch

                // Option B: If you added getIngredientsByContainerId to repository:
                /*
                try {
                    // Collect the first emission (or timeout) to get the list
                    val ingredientsToDelete = withTimeout(5000) { // 5 second timeout
                        repository.getIngredientsByContainerId(id).first()
                    }
                    Log.d(TAG, "Found ${ingredientsToDelete.size} ingredients to delete from Firestore")
                    for (ingredient in ingredientsToDelete) {
                        viewModelScope.launch {
                            syncDeletedIngredient(ingredient.ingredientID)
                        }
                    }
                } catch (e: TimeoutCancellationException) {
                    Log.e(TAG, "Timeout getting ingredients for container ID: $id for Firestore sync", e)
                } catch (e: Exception) {
                    Log.e(TAG, "Error getting ingredients for container ID: $id for Firestore sync", e)
                }
                */

            } catch (e: Exception) {
                // Catch errors during local delete or sync initiation
                Log.e(TAG, "Error in deleteAllGroceryAtContainer for container ID: $id", e)
                // Depending on requirements, you might want to retry or notify user
            }
        }
    }
    fun updateGrocery(grocery: IngredientEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateGrocery(grocery)
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<IngredientEntity>> {
        return repository.searchDatabase(searchQuery).asLiveData()
    }
}