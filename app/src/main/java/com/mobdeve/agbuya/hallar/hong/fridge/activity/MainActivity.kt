package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobdeve.agbuya.hallar.hong.fridge.R
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
            updateNavIcons(R.id.containersBtn)
        }

        binding.groceriesBtn.setOnClickListener {
            replaceFragment(GroceriesFragment())
            updateNavIcons(R.id.groceriesBtn)
        }

        binding.recipesBtn.setOnClickListener {
            replaceFragment(RecipesFragment())
            updateNavIcons(R.id.recipesBtn)
        }

        binding.profileBtn.setOnClickListener {
            replaceFragment(ProfileFragment())
            updateNavIcons(R.id.profileBtn)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

    private fun updateNavIcons(activeId: Int) {
        binding.containersBtn.setImageResource(
            if (activeId == R.id.containersBtn) R.mipmap.container_dark
            else R.mipmap.container_white
        )

        binding.groceriesBtn.setImageResource(
            if (activeId == R.id.groceriesBtn) R.mipmap.ingredients_dark
            else R.mipmap.ingredient_white
        )

        binding.recipesBtn.setImageResource(
            if (activeId == R.id.recipesBtn) R.mipmap.recipe_dark
            else R.mipmap.recipe_white
        )

        binding.profileBtn.setImageResource(
            if (activeId == R.id.profileBtn) R.mipmap.profile_dark
            else R.mipmap.profile_white
        )
    }
}
