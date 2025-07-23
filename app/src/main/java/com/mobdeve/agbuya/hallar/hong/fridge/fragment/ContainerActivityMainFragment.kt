package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.R

import com.mobdeve.agbuya.hallar.hong.fridge.adapter.ContainerActivityMainAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerDataHelper
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.ContainerSharedViewModel


class ContainerActivityMainFragment : Fragment() {
    companion object{
        val CONTAINERS_KEY : String = "CONTAINER_DATA_KEY"
        val CONTAINER_EDIT_NAME_KEY: String  = "CONTAINER_EDIT_NAME_KEY"
        val CONTAINER_ISCANCELED: String  = "CONTAINER_ISCANCELED"
        val ADD_RESULT : String = "ADD_RESULT"
        val EDIT_RESULT : String = "EDIT_RESULT"
    }
    private var _binding:ContainerActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ContainerSharedViewModel
    private lateinit var containerList:ArrayList<ContainerModel>

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

    fun loadHelperData(){
        containerList= ContainerDataHelper.Companion.initializeContainers(requireContext())

    }
    fun getContainerList():ArrayList<ContainerModel>{
        return containerList
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = ContainerActivityMainBinding.inflate(inflater, container, false)
        val view = binding.root

        if(savedInstanceState!= null){

            // Loads up the onDestroy of the parcelable ArrayList of the given sets of container
            if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                containerList = savedInstanceState.getParcelableArrayList<ContainerModel>(CONTAINERS_KEY, ContainerModel::class.java) ?: run {
                    ContainerDataHelper.Companion.initializeContainers(requireContext())
                }
            }

        }else{
            loadHelperData()
        }



        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ContainerSharedViewModel::class.java]
        viewModel.loadInitialData(requireContext())
        //TODO: delete clear() Testing if data empty
//        containerList.clear()
        if(containerList.isEmpty()){
            showEmptyState()
        }else{
            setupRecycler()

        }
        // Receives if it is cancelled, may need to change this

        //TODO: delete clear() Testing if data empty, utilize shared viewholders in the activity
        parentFragmentManager.setFragmentResultListener(ADD_RESULT, viewLifecycleOwner) { _, bundle ->
            val isCancelled = bundle.getBoolean(CONTAINER_ISCANCELED)
            if(!isCancelled){
                loadHelperData()
                setupRecycler()
            }
        }

        // Add button logic
        binding.addGroceriesBtn.setOnClickListener {
            val action = R.id.gotoContainerEdit
            val bundle = Bundle().apply {
                putInt("EDIT_TYPE", EditType.ADD.ordinal)
            }
            findNavController().navigate(action, bundle)
            loadHelperData()

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
        outState.putParcelableArrayList(CONTAINERS_KEY, containerList)

    }

    private fun setupTopBar() {

        binding.searchBarContainer.headerTitleTv.setText(R.string.add_container)
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

        binding.containerRecyclerView.visibility = View.VISIBLE
        binding.containerRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ContainerActivityMainAdapter(containerList) {
                val action = R.id.gotoContainerEdit
                val bundle = Bundle().apply {
                    putInt("EDIT_TYPE", EditType.EDIT.ordinal)
                }
                findNavController().navigate(action, bundle)
            }
        }


    }

}