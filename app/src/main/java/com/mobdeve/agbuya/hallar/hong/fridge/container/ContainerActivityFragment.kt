package com.mobdeve.agbuya.hallar.hong.fridge.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Container
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerActivityMainBinding


class ContainerActivityFragment : Fragment() {

    private var _binding:ContainerActivityMainBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = ContainerActivityMainBinding.inflate(inflater, container, false)
        val view = binding.root

        val postList:ArrayList<Container> = ContainerDataHelper.initializeContainers(requireContext())
        binding.apply {
            containerRecyclerView.adapter = ContainerActivityAdapter(postList)
            containerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
//        clickListener()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun clickListener() = with(binding){
//        goSecondFragment.setOnClickListener{
//            findNavController().navigate(R.id.action_global_secondFragment)
//        }
//    }
}