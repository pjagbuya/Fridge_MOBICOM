package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentAddGroceryBinding

class AddGroceryFragment : Fragment() {

    private var _binding: FragmentAddGroceryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddGroceryBinding.inflate(inflater, container, false)

        // spinner options for Grocery Categories
        val groceryCategories = listOf( "Meat / Seafood", "Vegetables", "Fruits", "Dairy", "Spices", "Beverages", "Snacks / Bakery")

        // spinner options for Grocery Units
        val groceryUnit = listOf("Pack", "Can", "Piece", "Bottle", "Jar", "Sack", "Box")

        // ArrayAdapter for Categories
        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groceryCategories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // ️ArrayAdapter for Units
        val unitAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groceryUnit)
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // bind to both spinners
        binding.addGroceryCategorySpinner.adapter = categoryAdapter
        binding.addGroceryUnitSpinner.adapter = unitAdapter

        binding.addGroceryCancelBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
