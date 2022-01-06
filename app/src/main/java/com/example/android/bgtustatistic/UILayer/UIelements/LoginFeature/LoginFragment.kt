package com.example.android.bgtustatistic.UILayer.UIelements.LoginFeature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginApi
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRepository
import com.example.android.bgtustatistic.DataLayer.RetrofitBuilder.ServiceBuilder
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.UIelements.PerformanceFragment
import com.example.android.bgtustatistic.UILayer.UIelements.isValid
import com.example.android.bgtustatistic.databinding.FragmentLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers

class LoginFragment : Fragment() {
    private var binding_ : FragmentLoginBinding? = null
    private lateinit var binding : FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelFactory: LoginViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding_ = FragmentLoginBinding.inflate(inflater)
        binding = binding_!!
        initViewModel()
        binding.loginButton.setOnClickListener {
            binding.run {
                viewModel.login(
                    username = usernameField.text.toString(),
                    password = passwordField.text.toString())
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observe(viewLifecycleOwner){
            if(it.isErrorOccurred){
                Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
            }
            if(it.isLogin){
                loginSuccessful()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }
    private fun loginSuccessful(){
        requireActivity().run {
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PerformanceFragment())
                .commit()
            bottomNav.selectedItemId = R.id.performance
            bottomNav.visibility = View.VISIBLE
        }
    }
    private fun initViewModel(){
        viewModelFactory = LoginViewModelFactory(
            LoginRepository(
                loginRemoteDataSource = LoginRemoteDataSource(
                    loginApi = ServiceBuilder.buildService(LoginApi::class.java),
                    ioDispatcher = Dispatchers.IO
                )
            )
        )
        viewModel = viewModelFactory.create(LoginViewModel::class.java)
    }
}