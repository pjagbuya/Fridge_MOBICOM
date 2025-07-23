package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.RecipeIngredientAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentAddRecipeFormBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel

class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddRecipeFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RecipeIngredientAdapter
    private val ingredientList = arrayListOf<RecipeModel.RecipeIngredient>()

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

        setupRecyclerView()
        observeIngredientResult()
        setupButtons()

        // Populate fields if editing
        editingRecipe?.let { recipe ->
            binding.recipeNameEt.setText(recipe.name)
            binding.recipeDescriptionEt.setText(recipe.description)
            ingredientList.clear()
            ingredientList.addAll(recipe.ingredients)
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        adapter = RecipeIngredientAdapter(
            ingredientList,
            { /* Delete logic if needed */ },
            false
        )
        binding.recipeIngredientListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.recipeIngredientListRv.adapter = adapter
    }

    private fun observeIngredientResult() {
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<ArrayList<RecipeModel.RecipeIngredient>>("ingredients")
            ?.observe(viewLifecycleOwner) { updatedIngredients ->
                ingredientList.clear()
                ingredientList.addAll(updatedIngredients)
                adapter.notifyDataSetChanged()
            }
    }

    private fun setupButtons() {
        binding.recipeSaveBtn.setOnClickListener {
            // TODO: Implement save logic
            findNavController().popBackStack()
        }

        binding.recipeCancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.recipeAddIngredientBtn.setOnClickListener {
            val action = AddRecipeFragmentDirections.actionAddRecipeFragmentToAddIngredient()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
