package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.RecipeIngredientAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentAddIngredientBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
import androidx.navigation.fragment.findNavController

class AddIngredient : Fragment() {

    private var _binding: FragmentAddIngredientBinding? = null
    private val binding get() = _binding!!

    private val ingredientList = arrayListOf<RecipeModel.RecipeIngredient>()
    private lateinit var adapter: RecipeIngredientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddIngredientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupButtons()
    }

    private fun setupRecyclerView() {
        adapter = RecipeIngredientAdapter(
            ingredientList,
            { position ->
                ingredientList.removeAt(position)
                adapter.notifyItemRemoved(position)
            },
            true
        )
        binding.addedIngredientsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.addedIngredientsRv.adapter = adapter
    }

    private fun setupButtons() {
        // Done button: send ingredient list back to AddRecipeFragment
        binding.doneBtn.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle
                ?.set("ingredients", ArrayList(ingredientList))
            findNavController().popBackStack()
        }

        // Add ingredient not found button
        binding.addIngredientNotFoundBtn.setOnClickListener {
            showAddIngredientModal()
        }
    }

    private fun showAddIngredientModal() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.modal_add_ingredient_not_found, null)

        val nameEt = dialogView.findViewById<EditText>(R.id.customIngredientNameEt)
        val amountEt = dialogView.findViewById<EditText>(R.id.customIngredientAmountEt)
        val unitSpinner = dialogView.findViewById<Spinner>(R.id.customIngredientUnitSp)
        val cancelBtn = dialogView.findViewById<Button>(R.id.customeIngredientCancelBtn)
        val addBtn = dialogView.findViewById<Button>(R.id.customIngredientAddBtn)

        // Populate the spinner with RecipeUnit values
        val units = RecipeModel.RecipeUnit.entries.map { it.displayName }
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, units
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        unitSpinner.adapter = spinnerAdapter

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        cancelBtn.setOnClickListener { dialog.dismiss() }

        addBtn.setOnClickListener {
            val name = nameEt.text.toString().trim()
            val amount = amountEt.text.toString().toDoubleOrNull()
            val unit = RecipeModel.RecipeUnit.entries[unitSpinner.selectedItemPosition]

            if (name.isEmpty() || amount == null) {
                Toast.makeText(requireContext(),
                    "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val newIngredient = RecipeModel.RecipeIngredient(
                    name = name,
                    amount = amount,
                    unit = unit,
                    isCustom = true
                )
                ingredientList.add(newIngredient)
                adapter.notifyItemInserted(ingredientList.size - 1)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
