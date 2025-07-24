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
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.RecipeIngredientAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentAddIngredientBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.SharedRecipeViewModel

class AddIngredient : Fragment() {

    private var _binding: FragmentAddIngredientBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RecipeIngredientAdapter
    private val sharedViewModel: SharedRecipeViewModel by activityViewModels()

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

        sharedViewModel.ingredients.observe(viewLifecycleOwner) { updatedList ->
            adapter.updateData(updatedList)
        }
    }

    private fun setupRecyclerView() {
        adapter = RecipeIngredientAdapter(
            arrayListOf(),
            { position -> sharedViewModel.removeIngredient(position) },
            true
        )
        binding.addedIngredientsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.addedIngredientsRv.adapter = adapter
    }

    private fun setupButtons() {
        binding.doneBtn.setOnClickListener {
            if (sharedViewModel.ingredients.value.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "No ingredients added!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Ingredients added to recipe", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

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
                sharedViewModel.addIngredient(newIngredient)
                Toast.makeText(requireContext(), "Ingredient added", Toast.LENGTH_SHORT).show()
                adapter.updateData(sharedViewModel.ingredients.value ?: arrayListOf())
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
