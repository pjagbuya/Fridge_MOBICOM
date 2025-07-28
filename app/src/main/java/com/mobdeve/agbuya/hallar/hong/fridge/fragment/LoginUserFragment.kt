package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentProfileUserBinding

class LoginUserFragment : Fragment() {

    private var _binding: FragmentProfileUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

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

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Display user's email as their name
            val userEmail = currentUser.email ?: "User"
            binding.profileName.text = getString(R.string.welcome_user, userEmail)
        } else {
            // User is not logged in, redirect to login screen
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_loginUserMain_to_profileMain)
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
