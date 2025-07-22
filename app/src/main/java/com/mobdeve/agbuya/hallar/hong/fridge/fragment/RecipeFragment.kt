package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.RecipeMainAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentRecipeMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
import androidx.navigation.fragment.findNavController

class RecipeMainFragment : Fragment() {

    private var _binding: FragmentRecipeMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter: RecipeMainAdapter

    private val recipeList = arrayListOf(
        RecipeModel(
            id = 1,
            name = "Pancakes",
            description = "Fluffy homemade pancakes",
            ingredients = arrayListOf(
                RecipeModel.RecipeIngredient("1","Flour", 2.0, RecipeModel.RecipeUnit.CUP),
                RecipeModel.RecipeIngredient("2","Eggs", 2.0, RecipeModel.RecipeUnit.PIECE)
            )
        ),
        RecipeModel(
            id = 2,
            name = "Adobo",
            description = "Classic Filipino dish",
            ingredients = arrayListOf(
                RecipeModel.RecipeIngredient("1","Chicken", 1.0, RecipeModel.RecipeUnit.KG),
                RecipeModel.RecipeIngredient("2","Soy Sauce", 1.0, RecipeModel.RecipeUnit.CUP)
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
        setupRecyclerView()
        setupAddRecipeButton()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeMainAdapter(recipeList) { recipe ->
            onRecipeSelected(recipe)
        }
        binding.RecipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.RecipeRecyclerView.adapter = recipeAdapter
    }

    private fun setupAddRecipeButton() {
        binding.addRecipeBtn.setOnClickListener {
            openAddRecipeForm()
        }
    }

    private fun openAddRecipeForm() {
        // Using NavController instead of manual fragment transaction
        findNavController().navigate(
            RecipeMainFragmentDirections.actionRecipeMainFragmentToAddRecipeFragment(null)
        )
    }

    private fun onRecipeSelected(recipe: RecipeModel) {
        // Passing recipe using Safe Args
        val action = RecipeMainFragmentDirections
            .actionRecipeMainFragmentToRecipeDetails(recipe)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
