package com.example.android.bgtustatistic.UILayer.UIelements.LoginFeature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRepository

class LoginViewModelFactory(
    private val loginRepository: LoginRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}