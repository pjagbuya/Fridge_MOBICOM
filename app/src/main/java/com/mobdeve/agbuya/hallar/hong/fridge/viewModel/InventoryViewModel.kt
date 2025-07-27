package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.agbuya.hallar.hong.fridge.Repository.InventoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: InventoryRepository): ViewModel() {

    private val _inviteResult = MutableStateFlow<Result<Long>?>(null)
    val inviteResult: StateFlow<Result<Long>?> = _inviteResult

    fun inviteMember(inventoryName: String, email: String, nickname: String) {
        viewModelScope.launch {
            val inventory = repository.getInventoryByName(inventoryName)
            if (inventory == null) {
                _inviteResult.value = Result.failure(Exception("Inventory not found"))
                return@launch
            }

            val result = repository.inviteMember(inventory.id, email, nickname)
            _inviteResult.value = result
        }
    }

    fun acceptInvite(memberId: Int) {
        viewModelScope.launch {
            repository.updateInviteStatus(memberId, "ACCEPTED")
        }
    }

    fun declineInvite(memberId: Int) {
        viewModelScope.launch {
            repository.updateInviteStatus(memberId, "DECLINED")
        }
    }
}
