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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.container.GroceryDataHelper
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.mobdeve.agbuya.hallar.hong.fridge.Repository.RecipeRepository
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserEntity
import com.mobdeve.agbuya.hallar.hong.fridge.repository.UserRepository
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.NavigationbarBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.GrocerySharedViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.UserViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.UserViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
@AndroidEntryPoint
class PaulMainActivity : AppCompatActivity() {
    companion object{
        const val EDIT_TYPE_KEY = "EDIT_TYPE"
        const val OWNER_INVENTORY_ID = "inv_sample_001"

    }
    private lateinit var  activityMainBinding : ActivityMainBinding

    private lateinit var navBarBinding: NavigationbarBinding
    lateinit var userViewModel: UserViewModel

    private val groceryViewModel: GrocerySharedViewModel by viewModels()
//    private val newContainerResultLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//        // Check to see if the result returned is appropriate (i.e. OK)
//        if (result.resultCode == RESULT_OK) {
//            // TODO get back the changes to the recycler view and correspondingly add it
//
//
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            val firestore = Firebase.firestore


            val userDao = AppDatabase.getInstance(this).userDao()
            val recipeDao = AppDatabase.getInstance(this).recipeDao()
            val containerDao = AppDatabase.getInstance(this).containerDao()

            val userRepository = UserRepository(userDao)
            val recipeRepository = RecipeRepository(recipeDao)
            val containerRepository = ContainerRepository(containerDao)

            val factory = UserViewModelFactory(userRepository, recipeRepository, containerRepository)
            userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]



            activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(activityMainBinding.root)
            Log.d("ViewModelTest", "GroceryViewModel instance: $groceryViewModel")
//            wipeAllData(applicationContext)
//            initializeUser(applicationContext)
            initSampleData(applicationContext)

            navBarBinding = activityMainBinding.navigationBar

            setupNavigation()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.dynamicContent) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    private fun wipeAllData(context: Context) {
        val db = AppDatabase.getInstance(context)
        CoroutineScope(Dispatchers.IO).launch {
            db.ingredientDao().deleteAll()
            db.containerDao().deleteAll()
            db.userDao().deleteAll()
            Log.d("SampleData", "All data wiped.")
        }
    }

//    private fun initializeUser(context: Context){
//        val db = AppDatabase.getInstance(context)
////        val inventoryDao = db.inventoryDao()
//        val userDao = db.userDao()
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val existingUsers = userDao.getAllUsers()
////            val existingInventory = inventoryDao.getOwnerInventory(OWNER_INVENTORY_ID)
//            if (existingUsers.isNotEmpty()) {
//                Log.d("INITIALIZED", "user and empty inventory initialized")
//                return@launch
//            }
//            val userIdLong = userDao.insertUser(
//                UserEntity(
//                    name = "default user",
//                )
//            )
//            val userId = userIdLong.toInt()
////            val inventoryIdLong = inventoryDao.insertInventory(
////                InventoryEntity(
////                    userId = userId,
////                    name = "default inventory",
////                    userAuthId = null,
////                    inventoryId = OWNER_INVENTORY_ID
////                )
////            )
////            val inventoryId = inventoryIdLong.toString()
//            initSampleData(userId,context)
//        }
//
//
//    }

//    private fun initSampleData(userId: Int, context: Context) {
//        val db = AppDatabase.getInstance(context)
//        val containerDao = db.containerDao()
//        val ingredientDao = db.ingredientDao()
//
//        CoroutineScope(Dispatchers.IO).launch {
//            // Insert 5 containers
//            val containerIds = (1..5).map {
//                val container = ContainerEntity(
//                    name = "Container $it",
//                    imageContainer = ImageContainer(
//                        resId = R.drawable.container_type_1_fridge,
//                        colorId = R.color.black
//                    ),
//                    currCap = 0,
//                    maxCap = 10,
//                    timeStamp = ContainerModel.getTimeStamp(),
//                    ownerUserId = userId
//                )
//                containerDao.insertContainer(container)
//            }
//
//            // Insert 5 ingredients (1 each container)
//            containerIds.forEachIndexed { index, containerId ->
//                val ingredient = IngredientEntity(
//                    name = "Ingredient ${index + 1}",
//                    iconResId = R.mipmap.ic_itemtype_other,
//                    price = 10.0,
//                    quantity = 10.0,
//                    unit = "kg",
//                    conditionType = "very ok",
//                    itemType = "VEGETABLE",
//                    dateAdded = "2025-07-15",
//                    expirationDate = "2025-08-15",
//                    attachedContainerId = containerId.toInt(),
//                    imageList = arrayListOf() // Or dummy ImageRaw
//                )
//                ingredientDao.insertAndUpdateCapacity(ingredient)
//            }
//        }
//    }

    private fun initSampleData(context: Context) {
        val db = AppDatabase.getInstance(context)
        val userDao = db.userDao()
        val containerDao = db.containerDao()
        val ingredientDao = db.ingredientDao()

        CoroutineScope(Dispatchers.IO).launch {

            val existingUsers = userDao.getAllUsers()
            if (existingUsers.isNotEmpty()) {
                Log.d("SampleData", "Sample data already initialized. Skipping.")
                return@launch
            }
            // Insert one user
            val userIdLong = userDao.insertUser(
                UserEntity(
                    name = "DEFAULT USER",
                )
            )
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
                    timeStamp = ContainerModel.getTimeStamp(),
                    ownerUserId = userId
                )
                containerDao.insertContainer(container)
            }

            // Insert 5 ingredients (1 each container)
            containerIds.forEachIndexed { index, containerId ->
                val ingredient = IngredientEntity(
                    name = "Ingredient ${index + 1}",
                    iconResId = R.mipmap.ic_itemtype_other,
                    price = 10.0,
                    quantity = 10.0,
                    unit = "kg",
                    conditionType = "very ok",
                    itemType = "VEGETABLE",
                    dateAdded = "2025-07-15",
                    expirationDate = "2025-08-15",
                    attachedContainerId = containerId.toInt(),
                    imageList = arrayListOf() // Or dummy ImageRaw
                )
                ingredientDao.insertAndUpdateCapacity(ingredient)
            }
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.dynamicContent) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            resetAllIcons()
            when (destination.id) {
                R.id.containerMain -> navBarBinding.containersBtn.setImageResource(R.mipmap.container_dark)
                R.id.groceriesMain -> navBarBinding.groceriesBtn.setImageResource(R.mipmap.ingredients_dark)
                R.id.recipeMainFragment -> navBarBinding.recipesBtn.setImageResource(R.mipmap.recipe_dark)
                R.id.profileMain -> navBarBinding.profileBtn.setImageResource(R.mipmap.profile_dark)
            }
        }


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
            navController.navigate(R.id.recipeMainFragment)
        }
        navBarBinding.profileBtn.setOnClickListener {
            resetAllIcons()
            navBarBinding.profileBtn.setImageResource(R.mipmap.profile_dark)
            navController.navigate(R.id.profileMain)
        }
    }

    fun resetAllIcons() {
        navBarBinding.containersBtn.setImageResource(R.mipmap.container_white)
        navBarBinding.groceriesBtn.setImageResource(R.mipmap.ingredient_white)
        navBarBinding.recipesBtn.setImageResource(R.mipmap.recipe_white)
        navBarBinding.profileBtn.setImageResource(R.mipmap.profile_white)
    }


}