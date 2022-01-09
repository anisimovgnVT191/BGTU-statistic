package com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRepository
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRepository

class ContingentViewModelFactory(
    private val contingentRepository: ContingentRepository,
    private val loginRepository: LoginRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ContingentViewModel::class.java)){
            return ContingentViewModel(contingentRepository, loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModl class")
    }
}