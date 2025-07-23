//package com.mobdeve.agbuya.hallar.hong.fridge.activity
//
//
//import android.os.Bundle
//import android.view.View
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.NavHostFragment
//import com.mobdeve.agbuya.hallar.hong.fridge.fragment.AddRecipeFragment
//import com.mobdeve.agbuya.hallar.hong.fridge.fragment.HeaderWithSearchBarFragment
//import com.mobdeve.agbuya.hallar.hong.fridge.R
//import com.mobdeve.agbuya.hallar.hong.fridge.fragment.ReusableRecyclerView
//import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityRecipeBinding
//import com.mobdeve.agbuya.hallar.hong.fridge.databinding.NavigationbarBinding
//import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
//import com.mobdeve.agbuya.hallar.hong.fridge.fragment.EmptyActivityFragment
//import com.mobdeve.agbuya.hallar.hong.fridge.fragment.RecipeDetails
//
//class RecipeActivity : AppCompatActivity(), ReusableRecyclerView.OnRecipeClickListener {
//    private lateinit var binding : ActivityRecipeBinding
//    private lateinit var navBarBinding: NavigationbarBinding
//
////    private val recipeList = ArrayList<RecipeModel>()
//
//    private val recipeList = arrayListOf(   //dummy
//        RecipeModel(
//            id = 1,
//            name = "Pancakes",
//            description = "Fluffy homemade pancakes",
//            ingredients = arrayListOf(
//                RecipeModel.RecipeIngredient(name = "Flour", amount = 2.0, unit = RecipeModel.RecipeUnit.CUP),
//                RecipeModel.RecipeIngredient(name = "Eggs", amount = 2.0, unit = RecipeModel.RecipeUnit.PIECE)
//            )
//        ),
//        RecipeModel(
//            id = 2,
//            name = "Adobo",
//            description = "Classic Filipino dish",
//            ingredients = arrayListOf(
//                RecipeModel.RecipeIngredient(name = "Chicken", amount = 1.0, unit = RecipeModel.RecipeUnit.KG),
//                RecipeModel.RecipeIngredient(name = "Soy Sauce", amount = 1.0, unit = RecipeModel.RecipeUnit.CUP)
//            )
//        ),
//        RecipeModel(
//            id = 3,
//            name = "Fried Rice",
//            description = "Quick and easy fried rice",
//            ingredients = arrayListOf(
//                RecipeModel.RecipeIngredient(name = "Cooked Rice", amount = 2.0, unit = RecipeModel.RecipeUnit.CUP),
//                RecipeModel.RecipeIngredient(name = "Carrots", amount = 1.0, unit = RecipeModel.RecipeUnit.PIECE)
//            )
//        )
//    )
//
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityRecipeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        loadHeader(HeaderWithSearchBarFragment())
//        loadDynamicContent()
//
//        //recipe form
//        binding.addRecipeBtn.setOnClickListener {
//            openAddRecipeForm()
//        }
//
//        //navigations
//        navBarBinding = binding.navigationBar
//        setupNavigation()
//    }
//
//
//    private fun setupNavigation() {
//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.fragmentContainer) as NavHostFragment
//        val navController = navHostFragment.navController
//
//
//        navBarBinding.containersBtn.setOnClickListener {
//            resetAllIcons()
//            navBarBinding.containersBtn.setImageResource(R.mipmap.container_dark)
//            navController.navigate(R.id.containerMain)
//        }
//        navBarBinding.groceriesBtn.setOnClickListener {
//            resetAllIcons()
//            navBarBinding.groceriesBtn.setImageResource(R.mipmap.ingredients_dark)
////            navController.navigate(R.id.groceriesMain)
//        }
//        navBarBinding.recipesBtn.setOnClickListener {
//            resetAllIcons()
//            navBarBinding.recipesBtn.setImageResource(R.mipmap.recipe_dark)
////            navController.navigate(R.id.recipeActivityFragmentMain)
//        }
//        navBarBinding.profileBtn.setOnClickListener {
//            resetAllIcons()
//            navBarBinding.profileBtn.setImageResource(R.mipmap.profile_dark)
////            navController.navigate(R.id.profileActivityFragmentMain)
//        }
//    }
//
//    fun resetAllIcons() {
//        navBarBinding.containersBtn.setImageResource(R.mipmap.container_white)
//        navBarBinding.groceriesBtn.setImageResource(R.mipmap.ingredient_white)
//        navBarBinding.recipesBtn.setImageResource(R.mipmap.recipe_white)
//        navBarBinding.profileBtn.setImageResource(R.mipmap.profile_white)
//    }
//
//
//    private fun loadHeader(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.headerContainer, fragment)
//            .commit()
//    }
//
//    private fun loadDynamicContent() {
//        val fragment: Fragment = if (recipeList.isEmpty()) {
//            EmptyActivityFragment.newInstance("Add your \nRecipes")
//        } else {
//            ReusableRecyclerView.newInstance(recipeList)
//        }
//
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainer, fragment)
//            .commit()
//    }
//
//    private fun loadContent(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainer, fragment)
//            .commit()
//    }
//
//    private fun openAddRecipeForm() {
//        //hide the addbutton and text
//        binding.addRecipeBtn.visibility = View.GONE
//        binding.addContainerTv.visibility = View.GONE
//        // remove header
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.headerContainer, Fragment()) // Empty Fragment
//            .commit()
//        binding.headerContainer.visibility = View.GONE
//
//        // Load AddRecipeFragment in the dynamic container
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainer, AddRecipeFragment())
//            .addToBackStack(null) // so we can go back
//            .commit()
//    }
//
//    //return to recipe main page when pressing back
//    override fun onBackPressed() {
//        if (supportFragmentManager.backStackEntryCount > 0) {
//            supportFragmentManager.popBackStack()
//
//            // restore header and add button
//            loadHeader(HeaderWithSearchBarFragment())
//            binding.headerContainer.visibility = View.VISIBLE
//            binding.addRecipeBtn.visibility = View.VISIBLE
//            binding.addContainerTv.visibility = View.VISIBLE
//        } else {
//            super.onBackPressed()
//        }
//    }
//
//    override fun onRecipeSelected(recipe: RecipeModel) {
//        // hide header and add button
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.headerContainer, Fragment()) // Empty Fragment
//            .commit()
//        binding.headerContainer.visibility = View.GONE
//        binding.addRecipeBtn.visibility = View.GONE
//        binding.addContainerTv.visibility = View.GONE
//
//        // open Recipe Details
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainer, RecipeDetails.newInstance(recipe))
//            .addToBackStack(null)
//            .commit()
//    }
//
//
//}
//
