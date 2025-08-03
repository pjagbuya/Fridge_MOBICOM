package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.agbuya.hallar.hong.fridge.R

import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentProfileMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.helper.SessionManager
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.UserViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.UserViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class ProfileFragment: Fragment() {
    private var _binding: FragmentProfileMainBinding? = null
    private val binding get() = _binding!!
//    private lateinit var viewModel: UserViewModel

    private val viewModel: UserViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val session = SessionManager(requireContext())
//        val isLoggedIn = session.getUserEmail() != null

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loggedInUser.collectLatest { user ->
                if (user != null) {
                    // User is logged in, navigate to logged-in UI
                    findNavController().navigate(ProfileFragmentDirections.actionProfileMainToLoginUserMain())
                } else {
                    // Show login/signup buttons
                    binding.loginBtn.setOnClickListener {
                        findNavController().navigate(R.id.action_profileMain_to_loginMain)
                    }

                    binding.signUpBtn.setOnClickListener {
                        findNavController().navigate(R.id.action_profileMain_to_signupMain)
                    }
                }
            }
        }
    }

//        if (currentUserId != null) {
//            // when user is logged in, redirect to login user main layout instead
//            view.post {
//                findNavController().navigate(ProfileFragmentDirections.actionProfileMainToLoginUserMain())
//            }
//        } else {
//            // set up login button
//            binding.loginBtn.setOnClickListener {
//                findNavController().navigate(R.id.action_profileMain_to_loginMain)
//            }
//
//            // set up sign up button
//            binding.signUpBtn.setOnClickListener {
//                findNavController().navigate(R.id.action_profileMain_to_signupMain)
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}