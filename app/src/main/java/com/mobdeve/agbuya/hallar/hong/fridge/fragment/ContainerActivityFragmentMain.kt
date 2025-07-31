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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.R

import com.mobdeve.agbuya.hallar.hong.fridge.adapter.ContainerActivityMainAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.ContainerSharedViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.GrocerySharedViewModel


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
    private lateinit var containerViewModel: ContainerSharedViewModel
    private lateinit var groceryViewModel: GrocerySharedViewModel

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
        containerViewModel = ViewModelProvider(this).get(ContainerSharedViewModel::class.java)
        groceryViewModel = ViewModelProvider(this).get(GrocerySharedViewModel::class.java)




        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopBar()
        //TODO: Now not using test data but live data of shared view models. Only looks at the data again
//        containerList.clear()
        containerViewModel.readAllData.observe(viewLifecycleOwner) { containerList ->
            if (containerList.isNullOrEmpty()) {
                // 🔴 LiveData is empty
                showEmptyState()
            } else {
                // ✅ LiveData has data
                setupRecycler()
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
    private fun setupRecycler() {

        // If empty activity is still hogging
        checkAndPlaceRecyclerViewSpot()
        val tempAdapter = ContainerActivityMainAdapter() {
            container->
            groceryViewModel.deleteAllGroceryAtContainer(container.containerId)
            containerViewModel.deleteContainer(container.containerId)
        }

        binding.containerRecyclerView.visibility = View.VISIBLE
        binding.containerRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tempAdapter


        }

        containerViewModel.readAllData.observe(viewLifecycleOwner, Observer { container ->
            tempAdapter.setData(container)
        })


    }

}