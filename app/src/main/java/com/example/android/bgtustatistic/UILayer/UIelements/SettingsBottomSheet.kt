package com.example.android.bgtustatistic.UILayer.UIelements

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.databinding.FragmentSettingsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingsBottomSheet: BottomSheetDialogFragment() {
    private var binding_: FragmentSettingsBottomSheetBinding? = null
    private lateinit var binding: FragmentSettingsBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding_ = FragmentSettingsBottomSheetBinding.inflate(inflater)
        binding = binding_!!

        binding.exitButton.setOnClickListener {
            Toast.makeText(requireContext(), "Exit button pressed", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
}