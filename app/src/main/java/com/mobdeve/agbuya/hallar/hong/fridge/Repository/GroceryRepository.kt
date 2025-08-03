package com.mobdeve.agbuya.hallar.hong.fridge.repository

import androidx.lifecycle.LiveData
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserDao
import com.mobdeve.agbuya.hallar.hong.fridge.dao.ContainerDao
import com.mobdeve.agbuya.hallar.hong.fridge.dao.IngredientDao
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GroceryRepository @Inject constructor(
    private val groceryDao: IngredientDao,
    private val userDao: UserDao
) {

    // Flow for observing all groceries
    val readAllData: Flow<List<IngredientEntity>> = groceryDao.getAllIngredients()

    suspend fun addGrocery(grocery: IngredientEntity) {
        groceryDao.insertAndUpdateCapacity(grocery)
    }

    // Flow for observing a single grocery item by ID
    fun findGrocery(id: Int): Flow<IngredientEntity> {
        return groceryDao.getIngredientById(id)
    }

    suspend fun updateGrocery(grocery: IngredientEntity) {
        groceryDao.updateIngredient(grocery)
    }

    suspend fun deleteAllGroceryAtContainer(containerId: Int) {
        groceryDao.deleteAllIngredientsByContainerId(containerId)
    }

    suspend fun deleteGrocery(groceryId: Int) {
        groceryDao.deleteIngredientById(groceryId)
    }
    fun getIngredientsByFirebaseUid(firebaseUid: String): Flow<List<IngredientEntity>> {
        return groceryDao.getAllIngredientsByFirebaseUid(firebaseUid)
    }
    fun searchDatabase(searchQuery : String) : Flow<List<IngredientEntity>> = groceryDao.searchContainers(searchQuery)
}