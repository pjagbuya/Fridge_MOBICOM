package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel

//so fragments in the Recipe will share data and know when it is updated
class SharedRecipeViewModel : ViewModel() {

    // LiveData to hold ingredients
    private val _ingredients = MutableLiveData<ArrayList<RecipeModel.RecipeIngredient>>(arrayListOf())
    val ingredients: LiveData<ArrayList<RecipeModel.RecipeIngredient>> = _ingredients

    fun addIngredient(ingredient: RecipeModel.RecipeIngredient) {
        val currentList = _ingredients.value ?: arrayListOf()
        currentList.add(ingredient)
        _ingredients.value = currentList
    }

    fun removeIngredient(position: Int) {
        val currentList = _ingredients.value ?: arrayListOf()
        if (position in currentList.indices) {
            currentList.removeAt(position)
            _ingredients.value = currentList
        }
    }

    fun clearIngredients() {
        _ingredients.value = arrayListOf()
    }

    fun setIngredients(list: ArrayList<RecipeModel.RecipeIngredient>) {
        _ingredients.value = list
    }
}
