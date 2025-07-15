package com.mobdeve.agbuya.hallar.hong.fridge.container

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.R

import com.mobdeve.agbuya.hallar.hong.fridge.adapter.ContainerActivityMainAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.BaseSearchbarContainerBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerActivityMainBinding


class ContainerActivityFragmentMain : Fragment() {
    companion object{
        val CONTAINERS_KEY : String = "CONTAINER_DATA_KEY"

    }
    private var _binding:ContainerActivityMainBinding? = null
    private val binding get() = _binding!!

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

    fun loadFunnyData(){
        containerList= ContainerDataHelper.initializeContainers(requireContext())

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                containerList = savedInstanceState.getParcelableArrayList<ContainerModel>(CONTAINERS_KEY, ContainerModel::class.java) ?: run {
                    ContainerDataHelper.initializeContainers(requireContext())
                }
            }

        }else{
            loadFunnyData()
        }


        binding.apply {
            addContainerBtn.setOnClickListener {

            }
        }


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopBar()
        setupRecycler()
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

        binding.searchBarContainer.containerHeaderTv.setText(R.string.add_container)
    }

    private fun setupRecycler() {


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

        binding.addContainerBtn.setOnClickListener {
            val action = R.id.gotoContainerEdit
            val bundle = Bundle().apply {
                putInt("EDIT_TYPE", EditType.ADD.ordinal)
            }
            findNavController().navigate(action, bundle)
        }
    }

}