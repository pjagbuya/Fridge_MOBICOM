package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentProfileLoginMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.UserViewModel
import kotlin.getValue
import com.mobdeve.agbuya.hallar.hong.fridge.converters.*
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreHelper.FirestoreHelper
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.ContainerSharedViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.GrocerySharedViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.SharedRecipeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentProfileLoginMainBinding
    private lateinit var auth: FirebaseAuth


    //viewModels
    private val viewModel: UserViewModel by activityViewModels()
    private val containerViewModel : ContainerSharedViewModel by activityViewModels()
    private val groceryViewModel : GrocerySharedViewModel by activityViewModels()
    private val recipeViewModel : SharedRecipeViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileLoginMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val userDao = AppDatabase.getInstance(requireContext()).userDao()
//        val recipeDao = AppDatabase.getInstance(requireContext()).recipeDao()
//        val containerDao = AppDatabase.getInstance(requireContext()).containerDao()
//
//        val userRepository = UserRepository(userDao)
//        val recipeRepository = RecipeRepository(recipeDao)
//        val containerRepository = ContainerRepository(containerDao)
//
//        val factory = UserViewModelFactory(userRepository, recipeRepository, containerRepository)
//        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        auth = FirebaseAuth.getInstance()

        // LOGIN button click
        binding.userLoginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {

                binding.loginErrorTv.text = getString(R.string.login_error_msg)
                binding.loginErrorTv.visibility = View.VISIBLE
            } else {
                binding.loginErrorTv.visibility = View.INVISIBLE
                loginUser(email, password)
            }
        }

        // GO TO SIGN UP button click
//        binding.userSignUpBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_loginMain_to_signupMain)
//        }
    }
    private fun loginUser(email: String, password: String) {
        binding.userLoginBtn.isEnabled = false
        binding.userLoginBtn.text = "Logging in..."

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        // Login locally first
                        viewModel.onLogin(firebaseUser.uid, firebaseUser.displayName)

                        // Small delay to ensure local user is created
                        lifecycleScope.launch {
                            kotlinx.coroutines.delay(500)
                            syncToFirestore()
                        }

                        Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginMain_to_loginUserMain)
                    }
                } else {
                    binding.loginErrorTv.text = "Login failed: ${task.exception?.message}"
                    binding.loginErrorTv.visibility = View.VISIBLE
                    binding.userLoginBtn.isEnabled = true
                    binding.userLoginBtn.text = "Login"
                }
            }
    }
    private fun syncToFirestore() {
        val firestoreHelper = FirestoreHelper(requireContext())

        lifecycleScope.launch {
            try {
                // Sync user data - ONE TIME sync, not continuous collection
                viewModel.loggedInUser.value?.let { userEntity ->
                    val firestoreUser = userEntity.toFirestoreUser()
                    firestoreHelper.syncToFirestore("users", userEntity.fireAuthId, firestoreUser)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }

        lifecycleScope.launch {
            try {
                // Sync containers - ONE TIME sync
                val containers = containerViewModel.readAllData.value
                containers.forEach { container ->
                    val firestoreContainer = container.toFirestoreContainer()
                    // Use container ID as document ID, not user ID
                    firestoreHelper.syncToFirestore("containers", container.containerId.toString(), firestoreContainer)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }

        lifecycleScope.launch {
            try {
                // Sync groceries - ONE TIME sync
                val groceries = groceryViewModel.readAllData.value
                groceries.forEach { grocery ->
                    val firestoreIngredient = grocery.toFirestoreIngredient()
                    // Use grocery ID as document ID, not user ID
                    firestoreHelper.syncToFirestore("ingredients", grocery.ingredientID.toString(), firestoreIngredient)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
