package com.mobdeve.agbuya.hallar.hong.fridge.sharedModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
import kotlinx.coroutines.Dispatchers

class GrocerySharedViewModel(application : Application): AndroidViewModel(application){
    val readAllData : LiveData<List<IngredientEntity>>
    private val repository: GroceryRepository

    init {
        val ingredientDao = AppDatabase.getInstance(application).ingredientDao()
        repository = GroceryRepository(ingredientDao)
        readAllData = repository.readAllData
    }

    fun addGrocery(grocery : IngredientEntity){
        // viewModel Scope a coroutine, Dispatchers.IO sets it as background process
        viewModelScope.launch(Dispatchers.IO) {
            repository.addGrocery(grocery)
        }
    }
    fun deleteGrocery(id : Int){
        // viewModel Scope a coroutine, Dispatchers.IO sets it as background process
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteGrocery(id)
        }
    }
    fun deleteAllGroceryAtContainer(id : Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllGroceryAtContainer(id)
        }
    }
    fun updateGrocery(grocery : IngredientEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateGrocery(grocery)
        }
    }

}