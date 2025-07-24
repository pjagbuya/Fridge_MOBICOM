package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.navigation.fragment.NavHostFragment
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.container.GroceryDataHelper
import com.mobdeve.agbuya.hallar.hong.fridge.database.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.NavigationbarBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.UserEntity
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.GrocerySharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaulMainActivity : AppCompatActivity() {
    companion object{
        const val EDIT_TYPE_KEY = "EDIT_TYPE"

    }
    private lateinit var  activityMainBinding : ActivityMainBinding
    private lateinit var navBarBinding : NavigationbarBinding

    private lateinit var  containerModels: ArrayList<ContainerModel>
    private lateinit var  groceryModels: ArrayList<Ingredient>
    private val groceryViewModel: GrocerySharedViewModel by viewModels()
    private val newContainerResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        // Check to see if the result returned is appropriate (i.e. OK)
        if (result.resultCode == RESULT_OK) {


        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)

            activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(activityMainBinding.root)
            Log.d("ViewModelTest", "GroceryViewModel instance: $groceryViewModel")

            initSampleData(applicationContext)
            groceryViewModel.loadGroceryList(applicationContext)
            groceryViewModel.groceryList.observe(this) { list ->
                groceryModels = ArrayList(list)
            }
            navBarBinding = activityMainBinding.navigationBar

            setupNavigation()
    }

    private fun initSampleData(context: Context) {
        val db = AppDatabase.getInstance(context)
        val userDao = db.userDao()
        val containerDao = db.containerDao()
        val ingredientDao = db.ingredientDao()

        CoroutineScope(Dispatchers.IO).launch {
            // Insert one user
            val userIdLong = userDao.insertUser(UserEntity(username = "Paul"))
            val userId = userIdLong.toInt() // Works safely now
            // Insert 5 containers
            val containerIds = (1..5).map {
                val container = ContainerEntity(
                    name = "Container $it",
                    imageContainer = ImageContainer(
                        resId = R.drawable.container_type_1_fridge,
                        colorId = R.color.black
                    ),
                    currCap = 0,
                    maxCap = 10,
                    timeStamp = System.currentTimeMillis().toString(),
                    ownerUserId = userId
                )
                containerDao.insertContainer(container)
            }

            // Insert 5 ingredients (1 each container)
            containerIds.forEachIndexed { index, containerId ->
                val ingredient = IngredientEntity(
                    name = "Ingredient ${index + 1}",
                    iconResId = R.drawable.ic_launcher_foreground,
                    price = 10.0,
                    quantity = 10.0,
                    unit = "kg",
                    conditionType = "Fresh",
                    itemType = "VEGETABLE",
                    dateAdded = "2025-07-15",
                    expirationDate = "2025-08-15",
                    attachedContainerId = containerId.toInt(),
                    imageList = arrayListOf() // Or dummy ImageRaw
                )
                ingredientDao.insertIngredient(ingredient)
            }
        }
    }


    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.dynamicContent) as NavHostFragment
        val navController = navHostFragment.navController


        navBarBinding.containersBtn.setOnClickListener {
            resetAllIcons()
            navBarBinding.containersBtn.setImageResource(R.mipmap.container_dark)
            navController.navigate(R.id.containerMain)
        }
        navBarBinding.groceriesBtn.setOnClickListener {
            resetAllIcons()
            navBarBinding.groceriesBtn.setImageResource(R.mipmap.ingredients_dark)
            navController.navigate(R.id.groceriesMain)
        }
        navBarBinding.recipesBtn.setOnClickListener {
            resetAllIcons()
            navBarBinding.recipesBtn.setImageResource(R.mipmap.recipe_dark)
//            navController.navigate(R.id.recipeActivityFragmentMain)
        }
        navBarBinding.profileBtn.setOnClickListener {
            resetAllIcons()
            navBarBinding.profileBtn.setImageResource(R.mipmap.profile_dark)
//            navController.navigate(R.id.profileActivityFragmentMain)
        }
    }


    fun resetAllIcons() {
        navBarBinding.containersBtn.setImageResource(R.mipmap.container_white)
        navBarBinding.groceriesBtn.setImageResource(R.mipmap.ingredient_white)
        navBarBinding.recipesBtn.setImageResource(R.mipmap.recipe_white)
        navBarBinding.profileBtn.setImageResource(R.mipmap.profile_white)
    }
//    private fun setupNavigation() {
//        with(activityMainBinding.navigationBar) {
//            containersBtn.setOnClickListener {
//                navigateToActivity(
//                    it,
//                    ContainersMainActivity::class.java,
//                    listOf(groceriesBtn, recipesBtn, profileBtn)
//                )
//            }
//
//            groceriesBtn.setOnClickListener {
//                navigateToActivity(
//                    it,
//                    GroceryMainActivity::class.java,
//                    listOf(containersBtn, recipesBtn, profileBtn)
//                )
//            }
//
//            recipesBtn.setOnClickListener {
//                navigateToActivity(
//                    it,
//                    null, // Not yet implemented
//                    listOf(containersBtn, groceriesBtn, profileBtn)
//                )
//            }
//
//            profileBtn.setOnClickListener {
//                navigateToActivity(
//                    it,
//                    null, // Not yet implemented
//                    listOf(containersBtn, groceriesBtn, recipesBtn)
//                )
//            }
//        }
//    }
//    private fun navigateToActivity(
//        currentButton: View,
//        targetActivity: Class<*>?,
//        otherButtons: List<View>
//    ) {
//        currentButton.isEnabled = false
//        otherButtons.forEach { it.isEnabled = true }
//
//        if (targetActivity != null) {
//            val intent = Intent(this, targetActivity)
//            val options = ActivityOptionsCompat.makeCustomAnimation(
//                this,
//                R.anim.slide_in_right,
//                R.anim.slide_out_left
//            )
//            startActivity(intent, options.toBundle())
//        }
//    }

}