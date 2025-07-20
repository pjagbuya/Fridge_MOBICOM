package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentAddIngredientBinding

class AddIngredient : Fragment() {

    private lateinit var binding: FragmentAddIngredientBinding
    private var onIngredientAdded: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddIngredientBinding.inflate(inflater, container, false)

        // When Done is clicked, return ingredient and go back
        binding.doneBtn.setOnClickListener {
            val ingredientName = "Example Ingredient" // Replace with selected item from RecyclerView
            onIngredientAdded?.invoke(ingredientName)
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    fun setOnIngredientAddedListener(listener: (String) -> Unit) {
        onIngredientAdded = listener
    }
}