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
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentProfileLoginMainBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentProfileLoginMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileLoginMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginMain_to_loginUserMain)
                } else {
                    binding.loginErrorTv.text = "Login failed: ${task.exception?.message}"
                    binding.loginErrorTv.visibility = View.VISIBLE
                }
            }
    }
}
