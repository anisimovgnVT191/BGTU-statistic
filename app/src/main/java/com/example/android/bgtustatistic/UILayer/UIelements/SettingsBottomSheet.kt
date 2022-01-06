package com.example.android.bgtustatistic.UILayer.UIelements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.android.bgtustatistic.DataLayer.UserManager.UserManager
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.UIelements.LoginFeature.LoginFragment
import com.example.android.bgtustatistic.databinding.FragmentSettingsBottomSheetBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

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
            requireActivity().run{
                lifecycleScope.launch {
                    UserManager.removeUser()
                }
                val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNav.visibility = View.GONE
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LoginFragment())
                    .commit()
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }
}