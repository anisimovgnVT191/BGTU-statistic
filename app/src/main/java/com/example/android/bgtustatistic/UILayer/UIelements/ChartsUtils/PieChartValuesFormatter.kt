package com.example.android.bgtustatistic.UILayer.UIelements.ChartsUtils

import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToInt

class PieChartValuesFormatter: ValueFormatter() {
    override fun getFormattedValue(value: Float) = if(value < 7F){
        ""
    }else {"${value.roundToInt()}%"}
}