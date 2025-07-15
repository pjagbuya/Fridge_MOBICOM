package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityLoginBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.MainViewModel

class LoginActivity: ComponentActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBackBtn.setOnClickListener({
            startActivity((Intent(this, ProfileFragment::class.java)))
        })
    }
}