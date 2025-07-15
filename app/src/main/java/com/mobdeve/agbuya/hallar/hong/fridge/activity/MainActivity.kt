package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.fragments.ContainerFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Default: Show container
        if (savedInstanceState == null) {
            replaceFragment(ContainerFragment())
        }

        // Clicks: Show other fragments
        binding.containersBtn.setOnClickListener {
            replaceFragment(ContainerFragment())
        }

        binding.groceriesBtn.setOnClickListener {
            replaceFragment(GroceriesFragment())
        }

        binding.recipesBtn.setOnClickListener {
            replaceFragment(RecipesFragment())
        }

        binding.profileBtn.setOnClickListener {
            replaceFragment(ProfileFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}
