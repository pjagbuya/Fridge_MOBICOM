package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserRepository
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentProfileSignupMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.UserViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.UserViewModelFactory
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {

    private var _binding: FragmentProfileSignupMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private var signupAttempt = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileSignupMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getInstance(requireContext())
        val repository = UserRepository(db.userDao())
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        binding.signUpBtn.setOnClickListener {
            val name = binding.userFullNameEt.text.toString()
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val confirmPassword = binding.confirmPasswordEt.text.toString()
            binding.signupErrorTv.visibility = View.INVISIBLE

            if (password != confirmPassword) {
                binding.signupErrorTv.visibility = View.VISIBLE
                return@setOnClickListener
            }

            signupAttempt = true
            val user = UserEntity(name = name, email = email, password = password)
            userViewModel.registerUser(user)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.registrationResult.collect { result ->
                if (signupAttempt) {  // only show error after signup attempt
                    result?.let {
                        if (it.isSuccess) {
                            binding.signupErrorTv.visibility = View.INVISIBLE
                            findNavController().navigate(R.id.loginMain)
                        } else {
                            binding.signupErrorTv.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
