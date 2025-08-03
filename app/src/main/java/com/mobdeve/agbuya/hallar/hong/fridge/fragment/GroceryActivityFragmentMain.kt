package com.mobdeve.agbuya.hallar.hong.fridge.fragment
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.GroceryActivityMainAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.container.GroceryDataHelper
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.BaseSearchbarBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.BaseSearchbarContainerBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceriesActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.ContainerSharedViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.GrocerySharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint

class GroceryActivityFragmentMain : Fragment() {
    companion object{
        const val SELECTED_INGREDIENT_KEY= "SELECTED_INGREDIENT_KEY"
        const val EDIT_INGREDIENT_KEY= "EDIT_INGREDIENT_KEY"
        const val ADD_INGREDIENT_KEY = "ADD_INGREDIENT_KEY"
    }
    private var _binding: GroceriesActivityMainBinding? = null
    private val binding get() = _binding!!



    private val groceryViewModel: GrocerySharedViewModel by viewModels()
    private lateinit var  searchBar : EditText

    private var originalGroceryList: List<IngredientEntity> = emptyList()
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

        val baseSearchBarContainerBinding : BaseSearchbarContainerBinding = binding.searchBarContainer
        val searchBarBinding : BaseSearchbarBinding = baseSearchBarContainerBinding.searchbar
        searchBar = searchBarBinding.searchTextEt
        searchBar.addTextChangedListener {
            val query = it.toString().trim()
            groceryViewModel.setSearchQuery(query)
        }
        setupSortDropdowns()
        setupRecycler()
        setupAddButton()

        // Launch lifecycle-aware collector for grocery list
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                groceryViewModel.filteredAndSortedData.collectLatest { updatedList ->
                    if (updatedList.isEmpty()) {
                        showEmptyState()
                    } else {
                        removeEmptyState()
                        binding.containerRecyclerView.visibility = View.VISIBLE
                        updateRecyclerView(updatedList)
                    }

                }
            }
        }
    }

    private fun setupAddButton(){
        binding.addGroceriesBtn.setOnClickListener {
            val action = R.id.gotoGroceriesAdd
            val bundle = Bundle().apply {
                putBoolean(EDIT_INGREDIENT_KEY, true)
                putBoolean(ADD_INGREDIENT_KEY, true)
            }
            findNavController().navigate(action, bundle)
        }
    }
    private fun removeEmptyState() {
        val existingFragment = childFragmentManager.findFragmentById(R.id.displayFrame)
        if (existingFragment is EmptyActivityFragment) {
            childFragmentManager.beginTransaction()
                .remove(existingFragment)
                .commit()
        }
    }
    private fun showEmptyState(){
        binding.containerRecyclerView.visibility = View.GONE
        val emptyFragment = EmptyActivityFragment.newInstance("Add your \nGroceries")
        childFragmentManager.beginTransaction()
            .replace(R.id.displayFrame, emptyFragment)
            .commit()
    }


    private fun updateRecyclerView(sortedList: List<IngredientEntity>) {
        adapter.setData(sortedList)
    }
    private fun setupSortDropdowns() {
        // Sort type (A-Z, Quantity, etc.)
        val sortOptions = resources.getStringArray(R.array.sort_by_array)
        val sortAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, sortOptions)
        binding.sortByDropdown.setAdapter(sortAdapter)

        // Item type (Vegetable, Meat, etc.)
        val itemTypes = resources.getStringArray(R.array.item_type_array)
        val typeAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, itemTypes)
        binding.itemTypeDropDowm.setAdapter(typeAdapter)
// Clear text only on touch (before selection)
        binding.sortByDropdown.setOnTouchListener { _, _ ->
            binding.sortByDropdown.text.clear()
            false // Let the click event continue (to show dropdown)
        }

// When an item is selected, do not clear
        binding.sortByDropdown.setOnItemClickListener { _, _, pos, _ ->
            val selectedSort = binding.sortByDropdown.adapter.getItem(pos).toString()
            groceryViewModel.setSort(selectedSort)
        }

// Same for itemTypeDropdown
        binding.itemTypeDropDowm.setOnTouchListener { _, _ ->
            binding.itemTypeDropDowm.text.clear()
            false
        }

        binding.itemTypeDropDowm.setOnItemClickListener { _, _, pos, _ ->
            val selectedType = binding.itemTypeDropDowm.adapter.getItem(pos).toString()
            groceryViewModel.setItemTypeFilter(selectedType)
        }
    }
    private fun applySorting() {
        val selectedSort = binding.sortByDropdown.text.toString()
        val selectedType = binding.itemTypeDropDowm.text.toString()

        var sortedList = groceryViewModel.readAllData.value ?: emptyList()

        // Filter by item type (only if not "All")
        if (selectedType.isNotEmpty() && selectedType != "All") {
            sortedList = sortedList.filter { it.itemType.equals(selectedType, ignoreCase = true) }
        }

        // Sorting of key attributes
        sortedList = when (selectedSort) {
            "A-Z" -> sortedList.sortedBy { it.name.lowercase() }
            "Quantity ↑" -> sortedList.sortedBy { it.quantity }
            "Quantity ↓" -> sortedList.sortedByDescending { it.quantity }
            "Date Added" -> sortedList.sortedByDescending { it.dateAdded }
            "Expiry" -> sortedList.sortedBy { it.expirationDate }
            else -> sortedList
        }

        updateRecyclerView(sortedList)
    }



    private fun setupRecycler() {
        adapter = GroceryActivityMainAdapter()
        binding.containerRecyclerView.adapter = adapter
        binding.containerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
