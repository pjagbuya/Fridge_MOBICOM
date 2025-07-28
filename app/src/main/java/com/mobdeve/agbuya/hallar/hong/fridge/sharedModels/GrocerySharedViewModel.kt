package com.mobdeve.agbuya.hallar.hong.fridge.sharedModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.database.AppDatabase
import kotlinx.coroutines.launch
import com.mobdeve.agbuya.hallar.hong.fridge.extensions.toDomainModel
class GrocerySharedViewModel : ViewModel(){
    private val _groceryList = MutableLiveData<List<Ingredient>>()
    val groceryList: LiveData<List<Ingredient>> get() = _groceryList
    fun loadGroceryList(context: Context) {
        val db = AppDatabase.getInstance(context)
        viewModelScope.launch {
            val allIngredients = db.ingredientDao().getAllIngredients()
            _groceryList.postValue(allIngredients.map { it.toDomainModel() })
        }
    }
}