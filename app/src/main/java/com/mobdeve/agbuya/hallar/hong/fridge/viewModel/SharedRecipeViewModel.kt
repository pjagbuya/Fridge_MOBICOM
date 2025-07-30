package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import androidx.lifecycle.*
import com.mobdeve.agbuya.hallar.hong.fridge.Mappers.toEntity
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
import com.mobdeve.agbuya.hallar.hong.fridge.repository.RecipeRepository
import kotlinx.coroutines.launch

class SharedRecipeViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _ingredients = MutableLiveData<ArrayList<RecipeModel.RecipeIngredient>>(arrayListOf())
    val ingredients: LiveData<ArrayList<RecipeModel.RecipeIngredient>> = _ingredients

    private val _saveResult = MutableLiveData<Boolean>()
    val saveResult: LiveData<Boolean> = _saveResult

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

    /**
     * Saves the recipe and its ingredients to the Room database.
     * @param name Name of the recipe
     * @param description Optional description
     * @param inventoryOwnerId FK to InventoryEntity
     */
    fun saveRecipe(name: String, description: String = "", inventoryOwnerId: String) {
        viewModelScope.launch {
            try {
                // Create the RecipeModel
                val model = RecipeModel(
                    name = name,
                    description = description,
                    inventoryOwnerId = inventoryOwnerId,
                    ingredients = _ingredients.value ?: arrayListOf()
                )

                // Convert to Room entities using your mappers
                val recipeEntity = model.toEntity()
                val insertedId = repository.insertRecipe(recipeEntity).toInt()

                val ingredientEntities = model.ingredients.map {
                    it.toEntity(insertedId)
                }

                // Insert ingredients
                repository.insertIngredients(ingredientEntities)

                // Success
                clearIngredients()
                _saveResult.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _saveResult.value = false
            }
        }
    }
}
