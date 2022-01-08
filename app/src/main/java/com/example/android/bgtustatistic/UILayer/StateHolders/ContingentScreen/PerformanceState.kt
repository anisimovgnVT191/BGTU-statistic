package com.example.android.bgtustatistic.UILayer.StateHolders.ContingentScreen

import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.ContingentMovement

data class PerformanceState(
    val contingentList: List<ContingentMovement>?
)