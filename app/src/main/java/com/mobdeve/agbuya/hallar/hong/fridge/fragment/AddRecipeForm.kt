package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.RecipeIngredientAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentAddRecipeFormBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
import com.mobdeve.agbuya.hallar.hong.fridge.repository.RecipeRepository
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.SharedRecipeViewModel
import kotlinx.coroutines.launch

class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddRecipeFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RecipeIngredientAdapter
    private lateinit var recipeRepository: RecipeRepository

    // Shared ViewModel
    private val sharedViewModel: SharedRecipeViewModel by activityViewModels()

    // Safe Args
    private val args: AddRecipeFragmentArgs by navArgs()
    private var editingRecipe: RecipeModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editingRecipe = args.recipeToEdit
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecipeFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeRepository = RecipeRepository(AppDatabase.getInstance(requireContext()).recipeDao())

        setupRecyclerView()
        setupButtons()

        // Observe the ingredient list from the shared ViewModel
        sharedViewModel.ingredients.observe(viewLifecycleOwner) { updatedList ->
            adapter.updateData(updatedList)
        }

        // If editing an existing recipe
        editingRecipe?.let { recipe ->
            binding.recipeNameEt.setText(recipe.name)
            binding.recipeDescriptionEt.setText(recipe.description)
            sharedViewModel.clearIngredients()
            sharedViewModel.ingredients.value?.addAll(recipe.ingredients)
            adapter.updateData(recipe.ingredients)
            sharedViewModel.setIngredients(ArrayList(recipe.ingredients))
        }
    }

    private fun setupRecyclerView() {
        adapter = RecipeIngredientAdapter(
            arrayListOf(),
            { position -> sharedViewModel.removeIngredient(position) },
            false
        )
        binding.recipeIngredientListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.recipeIngredientListRv.adapter = adapter
    }

    private fun setupButtons() {
        binding.recipeSaveBtn.setOnClickListener {
            saveRecipe()
        }
        binding.recipeCancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.recipeAddIngredientBtn.setOnClickListener {
            val action = AddRecipeFragmentDirections.actionAddRecipeFragmentToAddIngredient()
            findNavController().navigate(action)
        }
    }

    private fun saveRecipe() {
        val recipeId = sharedViewModel.currentRecipeId.value

        val name = binding.recipeNameEt.text.toString().trim()
        val description = binding.recipeDescriptionEt.text.toString().trim()
        val ingredients = sharedViewModel.ingredients.value ?: arrayListOf()

        if (name.isEmpty()) {
            binding.recipeNameEt.error = "Recipe name is required"
            return
        }



        viewLifecycleOwner.lifecycleScope.launch {
            try {
                if (recipeId != null) {
                    val existingRecipe = recipeRepository.getRecipeById(recipeId)
                    recipeRepository.saveRecipe(existingRecipe)
                    Toast.makeText(requireContext(), "edit successful", Toast.LENGTH_SHORT).show()
                    sharedViewModel.clearIngredients()
                    findNavController().popBackStack()
                } else {
                    val newRecipe = RecipeModel(
                        name = name,
                        description = description,
                        ingredients = ingredients
                    )
                    recipeRepository.saveRecipe(newRecipe)
                    Toast.makeText(requireContext(), "Recipe added successfully!", Toast.LENGTH_SHORT).show()
                    sharedViewModel.clearIngredients()
                    findNavController().popBackStack()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to add recipe. Please try again.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}