package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityRecipePageBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.MainViewModel

class RecipeActivity: ComponentActivity() {
    private lateinit var binding: ActivityRecipePageBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}