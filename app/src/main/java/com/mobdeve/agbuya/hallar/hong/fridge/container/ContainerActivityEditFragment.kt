package com.mobdeve.agbuya.hallar.hong.fridge.container

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.ContainerActivityAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.ContainerActivityEditAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerActivityEditBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel

class ContainerActivityEditFragment : Fragment(){
    companion object{
        val CONTAINERS_KEY : String = "CONTAINER_DATA_KEY"

    }
    private var _binding:ContainerActivityEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var containerList:ArrayList<ContainerModel>


    // These two inlines suppresses deprecation errors
    inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
        SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
    }
    inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
        SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
    }


    fun getcontainerList():ArrayList<ContainerModel>{
        return containerList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = ContainerActivityEditBinding.inflate(inflater, container, false)
        val view = binding.root


        containerList= ContainerDataHelper.containerModelsSelection


        binding.apply {
            containerRecyclerView.adapter = ContainerActivityEditAdapter(containerList){
                findNavController().navigate(R.id.gotoContainerMain)
            }
            containerRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(CONTAINERS_KEY, containerList)

    }

}