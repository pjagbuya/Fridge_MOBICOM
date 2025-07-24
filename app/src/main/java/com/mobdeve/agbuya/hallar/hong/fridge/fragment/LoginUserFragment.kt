package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentProfileUserBinding
import com.mobdeve.agbuya.hallar.hong.fridge.helper.SessionManager

class LoginUserFragment : Fragment() {

    private var _binding: FragmentProfileUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
        val name = session.getUserName()

        binding.profileName.text = getString(R.string.welcome_user, name)

        // clear session when logout
        binding.logoutButton.setOnClickListener {
            session.clearSession()
            findNavController().navigate(R.id.profileMain)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
