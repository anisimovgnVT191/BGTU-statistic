package com.example.android.bgtustatistic.UILayer.StateHolders.ContingentScreen

import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.ContingentMovement
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.MovementType

data class ContingentState(
    val contingentList: List<ContingentMovement>?,
    val decreaseTypes: List<MovementType>?,
    val increaseTypes: List<MovementType>?
)