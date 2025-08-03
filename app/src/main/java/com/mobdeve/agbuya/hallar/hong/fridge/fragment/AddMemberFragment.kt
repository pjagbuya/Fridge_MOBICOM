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
import com.mobdeve.agbuya.hallar.hong.fridge.Repository.InventoryRepository
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentProfileAddMemberBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.InventoryViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.InventoryViewModelFactory
import kotlinx.coroutines.launch

class AddMemberFragment: Fragment() {
    private var _binding: FragmentProfileAddMemberBinding? = null
    private val binding get() = _binding!!

    private lateinit var inventoryViewModel: InventoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileAddMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = AppDatabase.getInstance(requireContext()).inventoryDao()
        val repository = InventoryRepository(dao)
        val factory = InventoryViewModelFactory(repository)
        inventoryViewModel = ViewModelProvider(this, factory)[InventoryViewModel::class.java]

        binding.inviteBtn.setOnClickListener {

            val email = binding.emailInput.text.toString()
            val nickname = binding.nicknameInput.text.toString()
            val inventoryName = binding.inventoryNameInput.text.toString()
            val inventoryId = "tempID_not_yet_implemented"

            binding.addMemberErrorTv.visibility = View.INVISIBLE

            viewLifecycleOwner.lifecycleScope.launch {
//                inventoryViewModel.inviteMember(
//                    inventoryName, email, nickname, inventoryId
//                )
                //TODO

            }
        }

        // observe results
        viewLifecycleOwner.lifecycleScope.launch {
            inventoryViewModel.inviteResult.collect { result ->
                result?.onSuccess {
                    val email = binding.emailInput.text.toString()
                    val action = AddMemberFragmentDirections.actionUserAddMemberToUserMemberInvitation(email)
                    findNavController().navigate(action)

                    binding.addMemberErrorTv.visibility = View.INVISIBLE
                }?.onFailure {
                    binding.addMemberErrorTv.text = it.message
                    binding.addMemberErrorTv.visibility = View.VISIBLE
                }
            }
        }

        binding.cancelBtn.setOnClickListener({
            findNavController().navigate(R.id.loginUserMain)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}