package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.MainViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.ContainerAdapter

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //activities
        binding.profileBtn.setOnClickListener({
            startActivity(Intent(this, ProfileActivity::class.java))
        })
        binding.recipesBtn.setOnClickListener({
            startActivity(Intent(this, RecipeActivity::class.java))
        })
        binding.groceriesBtn.setOnClickListener({
            startActivity(Intent(this, GroceryActivity::class.java))
        })
        binding.addContainerBtn.setOnClickListener({
            startActivity(Intent(this, AddContainerActivity::class.java))
        })

        initContainer()
    }

    private fun initContainer() {
        viewModel.loadContainer().observe(this) { containerList ->
            binding.containerRecyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            val adapter = ContainerAdapter(containerList)
            binding.containerRecyclerView.adapter = adapter
        }
    }
}