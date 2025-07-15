package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentRecipeBinding
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.AddRecipeFragment
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.MainViewModel

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)

        binding.addRecipeBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AddRecipeFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }
}
