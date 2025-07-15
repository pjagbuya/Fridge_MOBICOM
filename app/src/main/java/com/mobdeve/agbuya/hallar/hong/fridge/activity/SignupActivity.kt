package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivitySignupBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.MainViewModel

class SignupActivity: ComponentActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupBackBtn.setOnClickListener({
            startActivity((Intent(this, ProfileFragment::class.java)))
        })
    }
}