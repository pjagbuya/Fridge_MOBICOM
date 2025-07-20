package com.mobdeve.agbuya.hallar.hong.fridge.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.GroceryActivityMainAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.container.GroceryDataHelper
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceriesActivityMainBinding


class GroceryActivityFragmentMain : Fragment() {

    private var _binding: GroceriesActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var groceryList: ArrayList<Ingredient>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GroceriesActivityMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groceryList = GroceryDataHelper.getSampleIngredients(requireContext()) // Fill with actual data from helper or ViewModel

        setupRecycler()
        // TODO: setup button transition
//        binding.addGroceryBtn.setOnClickListener {
//            findNavController().navigate(R.id.gotoGroceryEdit)
//        }
    }

    private fun setupRecycler() {

        binding.containerRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = GroceryActivityMainAdapter(groceryList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
