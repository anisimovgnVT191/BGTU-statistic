package com.example.android.bgtustatistic.UILayer.StateHolders.ContingentScreen

import com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen.SemesterFilter
import java.util.*

data class SettingsState(
    val yearFilter:Int = Calendar.getInstance().get(Calendar.YEAR),
    val semesterFilter: SemesterFilter = SemesterFilter.all
)