package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityGroceryPageBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.MainViewModel

class GroceryActivity: ComponentActivity() {
    private lateinit var binding: ActivityGroceryPageBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroceryPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //activities
        binding.profileBtn.setOnClickListener({
            startActivity(Intent(this, ProfileActivity::class.java))
        })
        binding.recipesBtn.setOnClickListener({
            startActivity(Intent(this, RecipeActivity::class.java))
        })
        binding.containersBtn.setOnClickListener({
            startActivity(Intent(this, MainActivity::class.java))
        })
    }
}