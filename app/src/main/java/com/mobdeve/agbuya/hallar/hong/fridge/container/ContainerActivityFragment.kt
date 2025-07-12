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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Container
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerActivityMainBinding
import kotlinx.serialization.Serializable


class ContainerActivityFragment : Fragment() {
    companion object{
        val CONTAINERS_KEY : String = "CONTAINER_DATA_KEY"

    }
    private var _binding:ContainerActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var postList:ArrayList<Container>


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
        postList= ContainerDataHelper.initializeContainers(requireContext())

    }
    fun getPostList():ArrayList<Container>{
        return postList
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = ContainerActivityMainBinding.inflate(inflater, container, false)
        val view = binding.root

        if(savedInstanceState!= null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                postList = savedInstanceState.getParcelableArrayList<Container>(CONTAINERS_KEY, Container::class.java) ?: run {
                    ContainerDataHelper.initializeContainers(requireContext())
                }
            }


        }else{
            loadFunnyData()

        }
        binding.apply {
            containerRecyclerView.adapter = ContainerActivityAdapter(postList)
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
        // Example: Save an ArrayList of Containers
        outState.putParcelableArrayList(CONTAINERS_KEY, postList)

    }

//    private fun clickListener() = with(binding){
//        goSecondFragment.setOnClickListener{
//            findNavController().navigate(R.id.action_global_secondFragment)
//        }
//    }
}