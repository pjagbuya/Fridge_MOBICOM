package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.PopupIconpickerLayoutBinding

class IconPickerDialogFragment(
    private val onIconSelected: (Int) -> Unit
) : DialogFragment() {

    private var _binding: PopupIconpickerLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = PopupIconpickerLayoutBinding.inflate(requireActivity().layoutInflater)
        val builder = AlertDialog.Builder(requireContext()).setView(binding.root)

        // Map each button to its corresponding mipmap resource ID
        val iconMap = mapOf(
            binding.otherBtn to R.mipmap.ic_itemtype_other,
            binding.fruitsBtn to R.mipmap.ic_itemtype_fruits,
            binding.vegetableBtn to R.mipmap.ic_itemtype_vegetable,
            binding.condimentsSeasoningBtn to R.mipmap.ic_itemtype_condiments_seasoning,
            binding.sweetsBtn to R.mipmap.ic_itemtype_sweets,
            binding.grainBreadBtn to R.mipmap.ic_itemtype_condiments_seasoning, // same icon reused
            binding.dairyBtn to R.mipmap.ic_itemtype_dairy,
            binding.cannedJarredBtn to R.mipmap.ic_itemtype_canned_jarred,
            binding.meatBtn to R.mipmap.ic_itemtype_meat,
            binding.beverageBtn to R.mipmap.ic_itemtype_beverage,
        )
        iconMap.forEach { (view, mipmapResId) ->
            view.setOnClickListener {
                onIconSelected(mipmapResId)
                dismiss()
            }
        }

        binding.exitBtn.setOnClickListener {
            dismiss()
        }

        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}