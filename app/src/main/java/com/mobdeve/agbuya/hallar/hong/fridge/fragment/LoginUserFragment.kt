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
    private val viewModel: UserViewModel by activityViewModels() // Keep using activityViewModels if needed across fragments

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

        binding.addMembersButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginUserMain_to_userAddMember)
        }

        binding.logoutButton.setOnClickListener {
            handleLogout()
        }

        // Collect user data for display
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.loggedInUser.collectLatest { userEntity ->
                    if (userEntity != null && !userEntity.fireAuthId.isNullOrEmpty()) {
                        // Use Firebase user's display name, falling back to local name if needed
                        val displayName = auth.currentUser?.displayName ?: userEntity.name ?: "User"
                        binding.profileName.text = getString(R.string.welcome_user, displayName)
                        Log.d("LoginUserFragment", "Displaying user: Name=$displayName, Auth ID=${userEntity.fireAuthId}")
                    } else {
                        // This state might be reached briefly during logout or if user data isn't loaded
                        Log.d("LoginUserFragment", "No user data available or user is null.")
                        binding.profileName.text = getString(R.string.welcome_user, "User") // Default text or hide
                    }
                }
            }
        }
    }

    private fun handleLogout() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // 1. Sign out from Firebase Auth
            auth.signOut()
            Log.d("LoginUserFragment", "Firebase signOut() called for user: ${currentUser.uid}")

            // 2. Clear local user data in ViewModel (Optional but recommended)
            // You might need a function in UserViewModel to clear the _user StateFlow
            // viewModel.clearLoggedInUser() // Example function you'd add to UserViewModel

            // 3. Clear other user-specific data in ViewModels (Optional but recommended)
            // e.g., containerViewModel.clearUserData(), groceryViewModel.clearUserData()
            // This would involve clearing the StateFlows/Lists in those ViewModels
            // that hold user-specific data.

            // 4. Navigate to login/profile screen
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            // Using popUpTo = 0 or inclusive = true in the nav graph action is often better
            // to clear the back stack up to the profile/login screen.
            viewModel.clearLoggedInUser()
            findNavController().navigate(R.id.action_loginUserMain_to_profileMain)
            // Alternatively, if profileMain is the start destination:
            // findNavController().popBackStack(R.id.profileMain, false) // Pop back to start, don't finish current (which is being popped by action anyway)

        } else {
            // User was already signed out somehow
            Log.d("LoginUserFragment", "Logout button clicked, but FirebaseAuth.currentUser is null.")
            Toast.makeText(requireContext(), "Already logged out", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_loginUserMain_to_profileMain)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}