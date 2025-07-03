package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityLoginPageBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.MainViewModel

class LoginActivity: ComponentActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener({
            startActivity((Intent(this, MainActivity::class.java)))
        })
    }
}

