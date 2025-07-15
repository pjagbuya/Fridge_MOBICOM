package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.FragmentAddContainerBinding

class AddContainerFragment : Fragment() {

    private var _binding: FragmentAddContainerBinding? = null
    private val binding get() = _binding!!

    private val iconList = listOf(
        R.mipmap.fridge_option_icon,
        R.mipmap.cabinet_icon_option,
        R.mipmap.freezer_option_icon
    )

    private var currentIndex = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddContainerBinding.inflate(inflater, container, false)

        binding.containerCancelBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Set initial icon
        binding.addContainerOptionIcon.setImageResource(iconList[currentIndex])

        binding.addContainerLeftBtn.setOnClickListener {
            currentIndex = if (currentIndex - 1 < 0) iconList.size - 1 else currentIndex - 1
            binding.addContainerOptionIcon.setImageResource(iconList[currentIndex])
        }

        binding.addContainerRightBtn.setOnClickListener {
            currentIndex = if (currentIndex + 1 >= iconList.size) 0 else currentIndex + 1
            binding.addContainerOptionIcon.setImageResource(iconList[currentIndex])
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
