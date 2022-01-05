package com.example.android.bgtustatistic.UILayer.UIelements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.databinding.FragmentLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginFragment : Fragment() {
    private var binding_ : FragmentLoginBinding? = null
    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding_ = FragmentLoginBinding.inflate(inflater)
        binding = binding_!!

        binding.loginButton.setOnClickListener {
            requireActivity().run {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, PerformanceFragment())
                    .commit()
                val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNav.selectedItemId = R.id.performance
                bottomNav.visibility = View.VISIBLE
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }
}