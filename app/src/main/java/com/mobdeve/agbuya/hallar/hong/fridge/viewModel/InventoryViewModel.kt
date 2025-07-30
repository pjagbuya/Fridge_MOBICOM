package com.mobdeve.agbuya.hallar.hong.fridge.viewmodels

import androidx.lifecycle.ViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.repository.InventoryRepository
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.InventoryEntity

class InventoryViewModel(private val repository: InventoryRepository) : ViewModel() {

    suspend fun getAllInventories(): List<InventoryEntity> {
        return repository.getAllInventories()
    }

}
