package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityAddRecipeBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.MainViewModel

class AddRecipeActivity: ComponentActivity() {
    private lateinit var binding: ActivityAddRecipeBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileBtn.setOnClickListener({
            startActivity(Intent(this, ProfileActivity::class.java))
        })
        binding.recipesBtn.setOnClickListener({
            startActivity(Intent(this, RecipeActivity::class.java))
        })
        binding.groceriesBtn.setOnClickListener({
            startActivity(Intent(this, GroceryActivity::class.java))
        })
    }
}