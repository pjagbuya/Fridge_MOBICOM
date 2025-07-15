package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivitySignupPageBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.MainViewModel

class SignupActivity: ComponentActivity() {
    private lateinit var binding: ActivitySignupPageBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener({
            startActivity((Intent(this, ProfileActivity::class.java)))
        })
    }
}