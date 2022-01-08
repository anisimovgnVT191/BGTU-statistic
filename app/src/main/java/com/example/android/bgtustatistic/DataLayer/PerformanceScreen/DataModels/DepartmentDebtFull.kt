package com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DataModels

import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DataModels.NumberDept

data class DepartmentDebtFull(
    val id: Int,
    val name: String,
    val number_depts: List<NumberDept>,
    val short_name: String
)