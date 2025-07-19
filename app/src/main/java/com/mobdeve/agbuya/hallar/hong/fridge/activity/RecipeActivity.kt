package com.mobdeve.agbuya.hallar.hong.fridge.activity


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.AddRecipeFragment
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

        //recipe form
        binding.addRecipeBtn.setOnClickListener {
            openAddRecipeForm()
        }

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

    private fun openAddRecipeForm() {
        //hide the addbutton and text
        binding.addRecipeBtn.visibility = View.GONE
        binding.addContainerTv.visibility = View.GONE
        // remove header
        supportFragmentManager.beginTransaction()
            .replace(R.id.headerContainer, Fragment()) // Empty Fragment
            .commit()
        binding.headerContainer.visibility = View.GONE

        // Load AddRecipeFragment in the dynamic container
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, AddRecipeFragment())
            .addToBackStack(null) // so we can go back
            .commit()
    }

    //return to recipe main page when pressing back
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()

            // restore header and add button
            loadHeader(HeaderWithSearchBarFragment())
            binding.headerContainer.visibility = View.VISIBLE
            binding.addRecipeBtn.visibility = View.VISIBLE
            binding.addContainerTv.visibility = View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }
}

