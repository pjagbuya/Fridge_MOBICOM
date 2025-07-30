package com.mobdeve.agbuya.hallar.hong.fridge.viewmodels

import androidx.lifecycle.*
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserEntity
import com.mobdeve.agbuya.hallar.hong.fridge.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _user = MutableLiveData<UserEntity?>()
    val user: LiveData<UserEntity?> = _user

    fun insertUser(userEntity: UserEntity) = viewModelScope.launch {
        repository.insertUser(userEntity)
    }

    fun loadUser(firebaseUid: String) = viewModelScope.launch {
        val fetchedUser = repository.getUserByFirebaseUid(firebaseUid)
        _user.postValue(fetchedUser)
    }
}
