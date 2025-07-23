package com.mobdeve.agbuya.hallar.hong.fridge.sharedModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerDataHelper
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel

class ContainerSharedViewModel: ViewModel(){
    private val _containerList = MutableLiveData<ArrayList<ContainerModel>>()
    val containerList: LiveData<ArrayList<ContainerModel>> get() = _containerList

    fun loadInitialData(context: Context) {
        if (_containerList.value == null) {
            _containerList.value = ContainerDataHelper.initializeContainers(context)
        }
    }

    fun refreshData(context: Context) {
        _containerList.value = ContainerDataHelper.initializeContainers(context)
    }

    fun setContainerList(newList: ArrayList<ContainerModel>) {
        _containerList.value = newList
    }
}