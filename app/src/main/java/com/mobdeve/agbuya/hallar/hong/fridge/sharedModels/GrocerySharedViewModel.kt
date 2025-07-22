package com.mobdeve.agbuya.hallar.hong.fridge.sharedModels

import androidx.lifecycle.ViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient

class GrocerySharedViewModel : ViewModel(){
    var groceryList: ArrayList<Ingredient> = ArrayList()
}