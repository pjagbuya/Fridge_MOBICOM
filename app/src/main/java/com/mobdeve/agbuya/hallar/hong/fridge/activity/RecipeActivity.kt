package com.mobdeve.agbuya.hallar.hong.fridge.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.HeaderWithSearchBarFragment
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.ReusableRecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityRecipeBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.EmptyActivityFragment

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRecipeBinding
//    private val recipeList = ArrayList<RecipeModel>()

    private val recipeList = arrayListOf(
        RecipeModel(1, "Pancakes"),
        RecipeModel(2, "Adobo"),
        RecipeModel(3, "Fried Rice")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadHeader(HeaderWithSearchBarFragment())
        loadDynamicContent()

    }

    private fun loadHeader(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.headerContainer, fragment)
            .commit()
    }

    private fun loadDynamicContent() {
        val fragment: Fragment = if (recipeList.isEmpty()) {
            EmptyActivityFragment.newInstance("Add your \nRecipes")
        } else {
            ReusableRecyclerView.newInstance(recipeList)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }



    private fun loadContent(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}