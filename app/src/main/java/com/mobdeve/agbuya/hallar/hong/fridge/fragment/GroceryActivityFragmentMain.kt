package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.GroceryActivityMainAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.BaseSearchbarContainerBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceriesActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.GrocerySharedViewModel


class GroceryActivityFragmentMain : Fragment() {
    companion object{
        const val SELECTED_INGREDIENT_KEY= "SELECTED_INGREDIENT_KEY"
        const val EDIT_INGREDIENT_KEY= "EDIT_INGREDIENT_KEY"
        const val ADD_INGREDIENT_KEY = "ADD_INGREDIENT_KEY"
    }
    private var _binding: GroceriesActivityMainBinding? = null
    private val binding get() = _binding!!
    private val groceryViewModel: GrocerySharedViewModel by activityViewModels()
    private val groceryList = mutableListOf<Ingredient>()
    private lateinit var adapter: GroceryActivityMainAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GroceriesActivityMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ViewModelTest", "GroceryViewModel instance: $groceryViewModel")

        val topBarBinding: BaseSearchbarContainerBinding = binding.searchBarContainer
        topBarBinding.headerTitleTv.setText(R.string.my_groceries)


        setupSortTypeDropDown()
        setupSortItemType()
        setupRecycler()

        // Make a mutable groceryList with reflected changes
        groceryViewModel.groceryList.observe(viewLifecycleOwner) { newList ->
            groceryList.clear()
            groceryList.addAll(newList)

            if (groceryList.isEmpty()) {
                showEmptyState()
            } else {
                binding.containerRecyclerView.visibility = View.VISIBLE
            }

            adapter.notifyDataSetChanged()
        }

        setupAddButton()

    }

    private fun setupAddButton(){
        binding.addGroceriesBtn.setOnClickListener {
            val action = R.id.gotoGroceriesEdit
            val bundle = Bundle().apply {
                putBoolean(EDIT_INGREDIENT_KEY, true)
                putBoolean(ADD_INGREDIENT_KEY, true)
            }
            findNavController().navigate(action, bundle)
        }
    }

    private fun showEmptyState(){
        binding.containerRecyclerView.visibility = View.GONE
        val emptyFragment = EmptyActivityFragment.newInstance("Add your \nGroceries")
        childFragmentManager.beginTransaction()
            .replace(R.id.displayFrame, emptyFragment)
            .commit()
    }

    private fun setupSortTypeDropDown()
    {
        val sortOptions = resources.getStringArray(R.array.sort_by_array)
        val sortAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, sortOptions)
        binding.sortByDropdown.setAdapter(sortAdapter)

        // TODO: Setup sorting algos for SortType
//        binding.sortByDropdown.setOnItemClickListener { _, _, position, _ ->
//            val selected = sortOptions[position]
//            // Handle sorting logic
//        }
    }
    private fun setupSortItemType()
    {
        val sortOptions = resources.getStringArray(R.array.item_type_array)
        val sortAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, sortOptions)
        binding.itemTypeDropDowm.setAdapter(sortAdapter)

        // TODO: Setup sorting algos for SortItemType
//        binding.sortByDropdown.setOnItemClickListener { _, _, position, _ ->
//            val selected = sortOptions[position]
//            // Handle sorting logic
//        }
    }

    private fun onComponentClickBehavior()
    {

    }
    private fun setupRecycler() {

        adapter = GroceryActivityMainAdapter(groceryList) { selectedIngredient ->
            val action = R.id.gotoGroceriesView
            val bundle = Bundle().apply {
                putParcelable(SELECTED_INGREDIENT_KEY, selectedIngredient)
                putBoolean(EDIT_INGREDIENT_KEY, false)
            }
            findNavController().navigate(action, bundle)
        }

        binding.containerRecyclerView.adapter = adapter
        binding.containerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
