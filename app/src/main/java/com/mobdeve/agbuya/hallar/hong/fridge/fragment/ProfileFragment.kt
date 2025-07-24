package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentProfileMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.helper.SessionManager

class ProfileFragment: Fragment() {
    private var _binding: FragmentProfileMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = SessionManager(requireContext())
        val isLoggedIn = session.getUserEmail() != null

        if (isLoggedIn) {
            // when user is logged in, redirect to login user main layout instead
            findNavController().navigate(R.id.loginUserMain)
        } else {
            // set up login button
            binding.loginBtn.setOnClickListener {
                findNavController().navigate(R.id.loginBtn)
            }

            // set up sign up button
            binding.signUpBtn.setOnClickListener {
                findNavController().navigate(R.id.signUpBtn)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}