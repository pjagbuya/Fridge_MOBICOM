package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.repository.RecipeRepository
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.RecipeIngredientAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentRecipeDetailsBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
import kotlinx.coroutines.launch

class RecipeDetails : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: RecipeDetailsArgs by navArgs()
    private lateinit var recipe: RecipeModel

    private lateinit var recipeRepository: RecipeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipe = args.recipe
        recipeRepository = RecipeRepository(
            AppDatabase.getInstance(requireContext()).recipeDao()
        )
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

        // Bind recipe details
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
            showDeleteButton = false
        )
    }

    private fun setupButtons() {
        binding.editRecipeBtn.setOnClickListener {
            val action = RecipeDetailsDirections
                .actionRecipeDetailsToAddRecipeFragment(recipe)
            findNavController().navigate(action)
        }

        binding.deleteRecipeBtn.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Recipe")
            .setMessage("Are you sure you want to delete '${recipe.name}'?")
            .setPositiveButton("Yes") { _, _ ->
                deleteRecipe()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteRecipe() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recipeRepository.deleteRecipe(recipe)
                Toast.makeText(requireContext(),
                    "Deleted ${recipe.name}", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(),
                    "Failed to delete recipe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
