package com.mobdeve.agbuya.hallar.hong.fridge

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mobdeve.agbuya.hallar.hong.fridge.activity.MainActivity
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityWelcomeBinding

class WelcomeActivity : ComponentActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.getStartedBtn.setOnClickListener({
            startActivity(Intent(this, MainActivity::class.java))
        })
    }
}