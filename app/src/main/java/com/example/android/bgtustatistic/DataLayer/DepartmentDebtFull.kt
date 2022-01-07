package com.example.android.bgtustatistic.DataLayer

data class DepartmentDebtFull(
    val id: Int,
    val name: String,
    val number_depts: List<NumberDept>,
    val short_name: String
)