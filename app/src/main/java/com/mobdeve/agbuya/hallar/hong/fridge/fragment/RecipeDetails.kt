package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.RecipeIngredientAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentRecipeDetailsBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel

class RecipeDetails : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!

    // Use Safe Args delegate
    private val args: RecipeDetailsArgs by navArgs()
    private lateinit var recipe: RecipeModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipe = args.recipe // Retrieve recipe using Safe Args
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recipeTitleTv.text = recipe.name
        binding.recipeDescriptionTv.text = recipe.description

        setupRecyclerView()
        setupButtons()
    }

    private fun setupRecyclerView() {
        binding.ingredientListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.ingredientListRv.adapter = RecipeIngredientAdapter(
            recipe.ingredients,
            onDeleteClick = {},
            showDeleteButton = false // hide trash button in detail view
        )
    }

    private fun setupButtons() {
        binding.editRecipeBtn.setOnClickListener {
            // Navigate to AddRecipeFragment, passing the recipe to edit
            val action = RecipeDetailsDirections
                .actionRecipeDetailsToAddRecipeFragment(recipe)
            findNavController().navigate(action)
        }

        binding.deleteRecipeBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Delete ${recipe.name}", Toast.LENGTH_SHORT).show()
            // TODO: Implement deletion logic
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
