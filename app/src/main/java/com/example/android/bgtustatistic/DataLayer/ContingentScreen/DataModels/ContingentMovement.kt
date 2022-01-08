package com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels

import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.Contingent

data class ContingentMovement(
    val contingent: List<Contingent>,
    val name_department: String,
    val short_name_department: String
)