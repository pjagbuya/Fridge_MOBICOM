package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.AddIngredient
import com.mobdeve.agbuya.hallar.hong.fridge.R

class AddRecipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_recipe_form, container, false)

        // find Add Ingredient Button
        val addIngredientBtn = view.findViewById<Button>(R.id.recipeAddIngredientBtn)
        addIngredientBtn.setOnClickListener {
            // load AddIngredientFragment into the same container
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AddIngredient())
                .addToBackStack(null) // allow user to return
                .commit()
        }

        return view
    }
}
