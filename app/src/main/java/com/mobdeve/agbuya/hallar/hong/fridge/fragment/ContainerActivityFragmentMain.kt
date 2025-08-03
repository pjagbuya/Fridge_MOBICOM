package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.R

import com.mobdeve.agbuya.hallar.hong.fridge.adapter.ContainerActivityMainAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.converters.toFirestoreContainer
import com.mobdeve.agbuya.hallar.hong.fridge.converters.toFirestoreIngredient
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.BaseSearchbarBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.BaseSearchbarContainerBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreHelper.FirestoreHelper
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.ContainerSharedViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.GrocerySharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContainerActivityFragmentMain : Fragment() {
    companion object{
        val CONTAINERS_KEY : String = "CONTAINER_DATA_KEY"
        val CONTAINER_EDIT_NAME_KEY: String  = "CONTAINER_EDIT_NAME_KEY"
        val CONTAINER_ISCANCELED: String  = "CONTAINER_ISCANCELED"
        val ADD_RESULT : String = "ADD_RESULT"
        val EDIT_RESULT : String = "EDIT_RESULT"
    }
    private var _binding:ContainerActivityMainBinding? = null
    private val binding get() = _binding!!
    private val containerViewModel: ContainerSharedViewModel by viewModels()
    private val groceryViewModel: GrocerySharedViewModel by viewModels()

    private val newContainerResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            // handle returned result if needed
        }
    }
    // These two inlines suppresses deprecation errors
    inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
        SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
    }
    inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
        SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = ContainerActivityMainBinding.inflate(inflater, container, false)
        val view = binding.root





        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopBar()


        val tempAdapter = ContainerActivityMainAdapter { container ->
            groceryViewModel.deleteAllGroceryAtContainer(container.containerId)
            containerViewModel.deleteContainer(container.containerId)
            val firestoreHelper = FirestoreHelper(requireContext())
            lifecycleScope.launch {
                try {
                    // Sync groceries - ONE TIME sync
                    val groceries = groceryViewModel.readAllData.value
                    groceries.forEach { grocery ->
                        val firestoreIngredient = grocery.toFirestoreIngredient()
                        // Use grocery ID as document ID, not user ID
                        firestoreHelper.syncToFirestore("ingredients", grocery.ingredientID.toString(), firestoreIngredient)
                    }
                } catch (e: Exception) {
                    // Handle error
                }
            }
            lifecycleScope.launch {
                try {
                    // Sync groceries - ONE TIME sync
                    val containers = containerViewModel.readAllData.value
                    containers.forEach { container ->
                        val firestoreContainer = container.toFirestoreContainer()
                        // Use grocery ID as document ID, not user ID
                        firestoreHelper.syncToFirestore("containers", container.containerId.toString(), firestoreContainer)
                    }
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }


        // Setup recycler below
        binding.containerRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tempAdapter
        }
        //TODO: Now not using test data but live data of shared view models. Only looks at the data again
//        containerList.clear()
        val searchBar = binding.searchBarContainer.searchbar.searchTextEt
        searchBar.addTextChangedListener {
            val query = it.toString().trim()
            containerViewModel.setSearchQuery(query)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                containerViewModel.filteredData.collect { containerList ->
                    if (containerList.isNullOrEmpty()) {
                        showEmptyState()
                    } else {
                        checkAndPlaceRecyclerViewSpot()
                        binding.containerRecyclerView.visibility = View.VISIBLE
                        tempAdapter.setData(containerList)
                    }
                }
            }
        }

        // Add button logic
        binding.addGroceriesBtn.setOnClickListener {
            val action = R.id.action_containerMain_to_containerAdd
            findNavController().navigate(action)

        }
    }

    private fun showEmptyState() {
        // Loads the empty fragment
        binding.containerRecyclerView.visibility = View.GONE
        val emptyFragment = EmptyActivityFragment.newInstance("Add your \nContainers")
        childFragmentManager.beginTransaction()
            .replace(R.id.containerFrame, emptyFragment)
            .commit()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putParcelableArrayList(CONTAINERS_KEY, containerList)

    }

    private fun setupTopBar() {

        binding.searchBarContainer.headerTitleTv.setText(R.string.container_header)
    }

    private fun checkAndPlaceRecyclerViewSpot(){

        // If empty activity is still hogging
        val existingEmptyFragment = childFragmentManager.findFragmentById(R.id.containerFrame)
        if (existingEmptyFragment is EmptyActivityFragment) {
            childFragmentManager.beginTransaction()
                .remove(existingEmptyFragment)
                .commit()
        }
    }


}