package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentGroceryBinding
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.AddGroceryFragment
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.MainViewModel

class GroceriesFragment : Fragment() {

    private var _binding: FragmentGroceryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGroceryBinding.inflate(inflater, container, false)

        binding.addGroceryBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AddGroceryFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }
}

