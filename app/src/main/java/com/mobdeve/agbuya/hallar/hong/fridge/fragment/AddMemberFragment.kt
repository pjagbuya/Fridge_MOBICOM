package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentProfileAddMemberBinding

class AddMemberFragment: Fragment() {
    private var _binding: FragmentProfileAddMemberBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileAddMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inviteBtn.setOnClickListener({
            findNavController().navigate(R.id.userMemberInvitation)
        })

        binding.cancelBtn.setOnClickListener({
            findNavController().navigate(R.id.loginUserMain)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}