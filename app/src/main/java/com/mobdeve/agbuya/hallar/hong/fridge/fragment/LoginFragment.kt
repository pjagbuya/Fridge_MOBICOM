package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserRepository
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentProfileLoginMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.UserViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.UserViewModelFactory
import androidx.navigation.fragment.findNavController
import com.mobdeve.agbuya.hallar.hong.fridge.helper.SessionManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentProfileLoginMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private var loginAttempt = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileLoginMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getInstance(requireContext())
        val repository = UserRepository(db.userDao())
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        val sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)

        binding.userLoginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            if (email.isBlank() || password.isBlank()) {
                binding.loginErrorTv.text = getString(R.string.login_error_msg)
                binding.loginErrorTv.visibility = View.VISIBLE
                return@setOnClickListener
            } else {
                binding.loginErrorTv.visibility = View.INVISIBLE
            }

            loginAttempt = true
            userViewModel.loginUser(email, password)
        }

        // observe login result
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.user.collectLatest { user ->
                if (user != null) {
                    val session = SessionManager(requireContext())
                    session.saveUserSession(user.email, user.name)
                    binding.loginErrorTv.visibility = View.INVISIBLE
                    findNavController().navigate(R.id.loginUserMain)
                } else {
                    if (loginAttempt) {
                        binding.loginErrorTv.visibility = View.VISIBLE
                    } else {
                        binding.loginErrorTv.visibility = View.INVISIBLE
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
