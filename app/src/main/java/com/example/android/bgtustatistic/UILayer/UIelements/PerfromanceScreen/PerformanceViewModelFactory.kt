package com.example.android.bgtustatistic.UILayer.UIelements.PerfromanceScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRepository
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtRepository

class PerformanceViewModelFactory(
    private val debtRepository: DebtRepository,
    private val loginRepository: LoginRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PerformanceViewModel::class.java)){
            return PerformanceViewModel(debtRepository, loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}