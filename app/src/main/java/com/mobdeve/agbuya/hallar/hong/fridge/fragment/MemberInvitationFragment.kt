package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentProfileMemberInvitationBinding

class MemberInvitationFragment: Fragment() {
    private var _binding: FragmentProfileMemberInvitationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileMemberInvitationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: MemberInvitationFragmentArgs by navArgs()
        val invitedEmail = args.invitedEmail
        binding.inviteConfirmationText.text = getString(R.string.member_invite_confirmation, invitedEmail)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}