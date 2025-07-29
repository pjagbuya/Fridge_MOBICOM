package com.mobdeve.agbuya.hallar.hong.fridge.repository

import androidx.lifecycle.LiveData
import com.mobdeve.agbuya.hallar.hong.fridge.dao.ContainerDao
import com.mobdeve.agbuya.hallar.hong.fridge.dao.IngredientDao
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity

class GroceryRepository(private val groceryDao: IngredientDao) {
    val readAllData : LiveData<List<IngredientEntity>> = groceryDao.getAllIngredients()

    suspend fun addGrocery(grocery : IngredientEntity){
        groceryDao.insertAndUpdateCapacity(grocery)
    }

    suspend fun updateGrocery(grocery: IngredientEntity){
        groceryDao.updateIngredient(grocery)
    }
}