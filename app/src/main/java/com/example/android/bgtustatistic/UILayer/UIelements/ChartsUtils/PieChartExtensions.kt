package com.example.android.bgtustatistic.UILayer

import android.graphics.Color
import com.github.mikephil.charting.charts.PieChart

fun PieChart.init(){
    legend.isEnabled = false
    isRotationEnabled = false
    setUsePercentValues(true)
    description.isEnabled = false
    setDrawEntryLabels(false)
    this.setTransparentCircleColor(Color.TRANSPARENT)
    transparentCircleRadius = -1F
}