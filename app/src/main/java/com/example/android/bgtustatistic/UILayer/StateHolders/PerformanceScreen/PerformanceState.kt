package com.example.android.bgtustatistic.UILayer.StateHolders.PerformanceScreen

import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DataModels.DepartmentDebt

data class PerformanceState(
    val relogined: Boolean,
    val debtsList: List<DepartmentDebt>? = null
)