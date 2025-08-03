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
import javax.inject.Inject

import kotlinx.coroutines.Dispatchers
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
            delay(5000) // Consistent delay

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

    fun syncDeletedIngredient(ingredientId: Int) {
        val TAG = "SYNC_DELETE"

        if (ingredientId <= 0) {
            Log.w(TAG, "Cannot sync delete for invalid ingredient ID: $ingredientId")
            return
        }

        val documentId = ingredientId.toString()

        viewModelScope.launch {
            Log.d(TAG, "Starting sync delete for ingredient ID: $ingredientId (Doc ID: $documentId)")
            delay(200) // Consistent delay
            Log.d(TAG, "Delay finished, attempting Firestore delete for Doc ID: $documentId")

            try {
                FirebaseFirestore.getInstance()
                    .collection("ingredients")
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "Successfully deleted ingredient document: $documentId from Firestore")
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Failed to delete ingredient document: $documentId from Firestore", exception)
                    }

            } catch (e: Exception) {
                Log.e(TAG, "Error initiating delete sync for ingredient ID: $ingredientId", e)
            } finally {
                Log.d(TAG, "Sync delete attempt finished for ingredient ID: $ingredientId")
            }
        }
    }
    fun syncNewlyAddedIngredient(addedItemName: String, context: Context) {
        val firestoreHelper = FirestoreHelper(context)

        viewModelScope.launch {
            delay(5000) // Give time for Room insert and Flow emission

            try {
                val currentGroceries = readAllData.value

                // Find the item we just added by name and valid ID
                val addedItem = currentGroceries.find { it.name == addedItemName && it.ingredientID > 0 }

                if (addedItem != null) {
                    syncSingleIngredientToFirestore(addedItem, firestoreHelper)
                } else {
                    // Optional: Handle case where item isn't found
                }
            } catch (e: CancellationException) {
                // viewModelScope job was cancelled
            } catch (e: Exception) {
                // Handle general errors
            }
        }
    }

    fun syncSingleIngredientToFirestore(ingredientEntity: IngredientEntity, firestoreHelper: FirestoreHelper) {
        viewModelScope.launch {
            try {
                if (ingredientEntity.ingredientID <= 0) {
                    return@launch
                }

                val firestoreIngredient = ingredientEntity.toFirestoreIngredient()
                val documentId = ingredientEntity.ingredientID.toString()

                firestoreHelper.syncToFirestore(
                    collectionName = "ingredients",
                    documentId = documentId,
                    data = firestoreIngredient,
                    successMessage = "Ingredient '${ingredientEntity.name}' synced successfully",
                    failureMessage = "Failed to sync ingredient '${ingredientEntity.name}'"
                )

            } catch (e: Exception) {
                // Handle errors during sync
            }
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

    fun deleteAllGroceryAtContainer(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllGroceryAtContainer(id)
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