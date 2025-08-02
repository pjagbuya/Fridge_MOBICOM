package com.mobdeve.agbuya.hallar.hong.fridge.sharedModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
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
    private val repository: GroceryRepository
) : ViewModel() {

    private val _selectedSortBy = MutableStateFlow("")
    private val _selectedItemType = MutableStateFlow("All items")

    val selectedSortBy: StateFlow<String> = _selectedSortBy
    val selectedItemType: StateFlow<String> = _selectedItemType

    // Expose all data
    val readAllData: StateFlow<List<IngredientEntity>> = repository.readAllData
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Combine filters and sorting
    val filteredAndSortedData: StateFlow<List<IngredientEntity>> = combine(
        repository.readAllData,
        _selectedSortBy,
        _selectedItemType
    ) { data, sortBy, itemType ->
        val filteredList = if (itemType != "All items") {
            data.filter { it.itemType.equals(itemType, ignoreCase = true) }
        } else data

        when (sortBy) {
            "A-Z" -> filteredList.sortedBy { it.name.lowercase() }
            "Quantity ↑" -> filteredList.sortedBy { it.quantity }
            "Quantity ↓" -> filteredList.sortedByDescending { it.quantity }
            "Date Added" -> filteredList.sortedByDescending { it.dateAdded }
            "Expiry" -> filteredList.sortedBy { it.expirationDate }
            else -> filteredList
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
}
