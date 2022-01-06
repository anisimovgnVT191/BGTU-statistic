package com.example.android.bgtustatistic.UILayer.UIelements.LoadingFeature

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.android.bgtustatistic.DataLayer.UserManager.UserManager
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.UIelements.LoginFeature.LoginFragment
import com.example.android.bgtustatistic.UILayer.UIelements.PerformanceFragment
import com.example.android.bgtustatistic.databinding.FragmentLoadingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers


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
        UserManager.initialize(//обязательно здесь и сейчас
            requireActivity().getSharedPreferences(
                getString(R.string.shared_preference_key_file),
                Context.MODE_PRIVATE),
            Dispatchers.IO
        )
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
                        findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE
                    }
                }else{
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, LoginFragment())
                        .commit()
                }

            }
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