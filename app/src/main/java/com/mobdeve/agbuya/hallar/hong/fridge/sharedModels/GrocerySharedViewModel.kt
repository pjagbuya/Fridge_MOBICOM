package com.mobdeve.agbuya.hallar.hong.fridge.sharedModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserDao
import com.mobdeve.agbuya.hallar.hong.fridge.dao.IngredientDao
import kotlinx.coroutines.launch
import com.mobdeve.agbuya.hallar.hong.fridge.extensions.toDomainModel
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import com.mobdeve.agbuya.hallar.hong.fridge.repository.GroceryRepository
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
@HiltViewModel
class GrocerySharedViewModel @Inject constructor(
    private val repository: GroceryRepository,
    private val userDao: UserDao, // Inject UserDao
    private val auth: FirebaseAuth // Inject FirebaseAuth
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