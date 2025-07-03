package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityProfilePageBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.MainViewModel

class ProfileActivity: ComponentActivity() {
    private lateinit var binding: ActivityProfilePageBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}