package com.example.android.bgtustatistic.UILayer.UIelements.LoadingFeature

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android.bgtustatistic.DataLayer.UserManager.UserManager
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.UIelements.LoginFeature.LoginFragment
import com.example.android.bgtustatistic.UILayer.UIelements.PerfromanceScreen.PerformanceFragment
import com.example.android.bgtustatistic.databinding.FragmentLoadingBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoadingFragment : Fragment() {
    private var _binding : FragmentLoadingBinding? = null
    private lateinit var binding: FragmentLoadingBinding
    private lateinit var viewModel: LoadingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingBinding.inflate(inflater)
        binding = _binding!!
        lifecycleScope.launch {
            UserManager.initialize(//обязательно здесь и сейчас
                requireActivity().getSharedPreferences(
                    getString(R.string.shared_preference_key_file),
                    Context.MODE_PRIVATE),
                Dispatchers.IO
            )
        }
        viewModel = ViewModelProvider(this)[LoadingViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isUserCashed.observe(viewLifecycleOwner){
            it.isUserCashed?.let {
                if(it){
                    requireActivity().run {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, PerformanceFragment())
                            .commit()
                        makeBarsVisible(this)
                    }
                }else{
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, LoginFragment())
                        .commit()
                }

            }
        }
    }

    private fun makeBarsVisible(activity: FragmentActivity){
        activity.apply {
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav.selectedItemId = R.id.performance
            bottomNav.visibility = View.VISIBLE
            findViewById<AppBarLayout>(R.id.top_bar_container).visibility = View.VISIBLE
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.userIsCashed()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}