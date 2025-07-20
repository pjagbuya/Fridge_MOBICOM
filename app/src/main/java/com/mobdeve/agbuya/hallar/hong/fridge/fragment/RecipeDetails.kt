package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.RecipeIngredientAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel

class RecipeDetails : Fragment() {

    private var recipe: RecipeModel? = null

    companion object {
        private const val ARG_RECIPE = "recipe"

        fun newInstance(recipe: RecipeModel): RecipeDetails {
            val fragment = RecipeDetails()
            val bundle = Bundle()
            bundle.putParcelable(ARG_RECIPE, recipe)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recipe = it.getParcelable(ARG_RECIPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_details, container, false)

        val titleTv: TextView = view.findViewById(R.id.recipeTitle)
        val descTv: TextView = view.findViewById(R.id.recipeDescription)
        val editBtn: Button = view.findViewById(R.id.editRecipeBtn)
        val deleteBtn: Button = view.findViewById(R.id.deleteRecipeBtn)
        val ingredientsRv: RecyclerView = view.findViewById(R.id.ingredientListRv)

        titleTv.text = recipe?.name
        descTv.text = recipe?.description


        val ingredients = recipe?.ingredients ?: arrayListOf()
        ingredientsRv.adapter = RecipeIngredientAdapter(ingredients)
        ingredientsRv.layoutManager = LinearLayoutManager(requireContext())

        editBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Edit ${recipe?.name}", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to edit form
        }

        deleteBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Delete ${recipe?.name}", Toast.LENGTH_SHORT).show()
            // TODO: Implement deletion
        }

        return view
    }
}
