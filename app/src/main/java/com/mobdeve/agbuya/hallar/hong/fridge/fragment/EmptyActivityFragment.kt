package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.EmptyTemplateBinding

class EmptyActivityFragment : Fragment() {
    companion object {
        private const val ARG_MESSAGE = "ARG_MESSAGE"

        fun newInstance(message: String): EmptyActivityFragment {
            val fragment = EmptyActivityFragment()
            val args = Bundle()
            args.putString(ARG_MESSAGE, message)
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: EmptyTemplateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = EmptyTemplateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = arguments?.getString(ARG_MESSAGE) ?: "Empty"
        binding.placeholderEmptyTv.text = message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}