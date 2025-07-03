package com.mobdeve.agbuya.hallar.hong.fridge

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobdeve.agbuya.hallar.hong.fridge.activity.MainActivity
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.MainViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityWelcomePageBinding

class WelcomeActivity : ComponentActivity() {
    private lateinit var binding: ActivityWelcomePageBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.getStartedBtn.setOnClickListener({
            startActivity(Intent(this, MainActivity::class.java))
        })
    }
}