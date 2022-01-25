package com.example.android.bgtustatistic.UILayer.StateHolders.PerformanceScreen

import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DataModels.DepartmentDebt
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DataModels.DepartmentDebtFull

data class PerformanceState(
    val relogined: Boolean,
    val debtsList: List<DepartmentDebt>? = null,
    val noDataIsShowing: Boolean = true,
    val debtsByIdList: List<DepartmentDebtFull>? = null
)