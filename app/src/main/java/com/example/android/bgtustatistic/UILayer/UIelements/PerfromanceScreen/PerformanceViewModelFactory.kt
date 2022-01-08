package com.example.android.bgtustatistic.UILayer.UIelements.PerfromanceScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtRepository

class PerformanceViewModelFactory(
    private val debtRepository: DebtRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PerformanceViewModel::class.java)){
            return PerformanceViewModel(debtRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}