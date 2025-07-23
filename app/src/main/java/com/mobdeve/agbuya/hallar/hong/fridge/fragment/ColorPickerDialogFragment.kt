package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.PopupColorpickerLayoutBinding

class ColorPickerDialogFragment(
    private val onColorSelected: (Int) -> Unit
) : DialogFragment() {

    private var _binding: PopupColorpickerLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        _binding = PopupColorpickerLayoutBinding.inflate(inflater)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        // Example: handle buttons inside the dialog
        val colorMap = mapOf(
            binding.colorRedBtn to R.color.red,
            binding.colorBlueBtn to R.color.blue,
            binding.colorOrangeBtn to R.color.orange,
            binding.colorPurpleBtn to R.color.purple,
            binding.colorMagentaBtn to R.color.magenta,
            binding.colorPinkBtn to R.color.pink,
            binding.colorYellowBtn to R.color.yellow,
            binding.colorBlackBtn to R.color.black
        )

        colorMap.forEach { (view, colorRes) ->
            view.setOnClickListener {
                val colorInt = ContextCompat.getColor(requireContext(), colorRes)
                onColorSelected(colorInt)
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