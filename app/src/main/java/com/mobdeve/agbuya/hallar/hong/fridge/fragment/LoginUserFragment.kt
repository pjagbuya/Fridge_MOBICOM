package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentProfileUserBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class LoginUserFragment : Fragment() {
    private var _binding: FragmentProfileUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

//    private lateinit var viewModel: UserViewModel
    private val viewModel: UserViewModel by activityViewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileUserBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.loggedInUser.collectLatest { userEntity ->
                    userEntity?.let {
                        if (!it.fireAuthId.isNullOrEmpty()) {
                            val userNameFromRoom = auth.currentUser?.displayName
                            val userAuthIdFromRoom = it.fireAuthId
                            binding.profileName.text = getString(R.string.welcome_user, userNameFromRoom)
                            Log.d("LoginUserFragment", "Name=$userNameFromRoom, Auth ID=$userAuthIdFromRoom")
                        } else {
                            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_loginUserMain_to_profileMain)
                        }
                    } ?: run {
                        Log.d("LoginUserFragment", "Local UserEntity is currently null.")
                        Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginUserMain_to_profileMain)
                    }
                }
            }
        }


        binding.addMembersButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginUserMain_to_userAddMember)
        }

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_loginUserMain_to_profileMain)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
