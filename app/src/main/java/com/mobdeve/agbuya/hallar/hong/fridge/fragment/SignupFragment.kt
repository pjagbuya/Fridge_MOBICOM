package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentProfileSignupMainBinding

class SignupFragment : Fragment() {

    private var _binding: FragmentProfileSignupMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileSignupMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.signUpBtn.setOnClickListener {
            val fullName = binding.userFullNameEt.text.toString().trim()
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString()
            val confirmPassword = binding.confirmPasswordEt.text.toString()

            binding.signupErrorTv.visibility = View.INVISIBLE

            // Form validation
            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                binding.signupErrorTv.text = getString(R.string.signup_error_msg)
                binding.signupErrorTv.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.signupErrorTv.text = "Passwords do not match"
                binding.signupErrorTv.visibility = View.VISIBLE
                return@setOnClickListener
            }

            registerUser(fullName, email, password)
        }
    }

    private fun registerUser(fullName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Set display name (optional)
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()
                    user?.updateProfile(profileUpdates)

                    Toast.makeText(requireContext(), "Sign up successful!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signupMain_to_loginMain)
                } else {
                    binding.signupErrorTv.text = "Sign up failed: ${task.exception?.message}"
                    binding.signupErrorTv.visibility = View.VISIBLE
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
