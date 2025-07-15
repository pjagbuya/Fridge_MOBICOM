package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityAddContainerBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.MainViewModel

class AddContainerActivity: ComponentActivity() {
    private lateinit var binding: ActivityAddContainerBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddContainerBinding.inflate(layoutInflater)
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