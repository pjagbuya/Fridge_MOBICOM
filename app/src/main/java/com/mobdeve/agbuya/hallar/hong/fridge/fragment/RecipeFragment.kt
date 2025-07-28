package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.Repository.RecipeRepository
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.RecipeMainAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentRecipeMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel.RecipeIngredient
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel.RecipeUnit
import kotlinx.coroutines.launch

class RecipeMainFragment : Fragment() {

    private var _binding: FragmentRecipeMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter: RecipeMainAdapter
    private lateinit var recipeRepository: RecipeRepository

    // Sample data for seeding
    private val sampleRecipes = arrayListOf(
        RecipeModel(
            name = "Pancakes",
            description = "Fluffy homemade pancakes",
            ingredients = arrayListOf(
                RecipeIngredient(name = "Flour", amount = 2.0, unit = RecipeUnit.CUP),
                RecipeIngredient(name = "Eggs", amount = 2.0, unit = RecipeUnit.PIECE)
            )
        ),
        RecipeModel(
            name = "Adobo",
            description = "Classic Filipino dish",
            ingredients = arrayListOf(
                RecipeIngredient(name = "Chicken", amount = 1.0, unit = RecipeUnit.KG),
                RecipeIngredient(name = "Soy Sauce", amount = 1.0, unit = RecipeUnit.CUP)
            )
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipeRepository = RecipeRepository(AppDatabase.getInstance(requireContext()).recipeDao())
        setupRecyclerView()
        setupAddRecipeButton()
        loadRecipes()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeMainAdapter(arrayListOf()) { recipe ->
            onRecipeSelected(recipe)
        }
        binding.recipeListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.recipeListRv.adapter = recipeAdapter
    }

    private fun setupAddRecipeButton() {
        binding.addRecipeBtn.setOnClickListener {
            openAddRecipeForm()
        }
    }

    private fun openAddRecipeForm() {
        findNavController().navigate(
            RecipeMainFragmentDirections.actionRecipeMainFragmentToAddRecipeFragment(null)
        )
    }

    private fun onRecipeSelected(recipe: RecipeModel) {
        val action = RecipeMainFragmentDirections
            .actionRecipeMainFragmentToRecipeDetails(recipe)
        findNavController().navigate(action)
    }

    private fun loadRecipes() {
        viewLifecycleOwner.lifecycleScope.launch {
            recipeRepository.seedData(sampleRecipes)
            val recipes = recipeRepository.getAllRecipes()
            recipeAdapter.updateData(recipes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
