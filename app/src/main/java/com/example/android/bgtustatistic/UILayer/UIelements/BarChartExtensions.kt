package com.example.android.bgtustatistic.UILayer

import com.github.mikephil.charting.charts.BarChart

fun BarChart.makeOnlyBarsVisible(){
    description.isEnabled = false
    axisLeft.setDrawLabels(false)
    axisRight.setDrawLabels(false)
    xAxis.setDrawGridLines(false)
    axisLeft.setDrawGridLines(false)
    axisRight.setDrawGridLines(false)
    axisLeft.setDrawAxisLine(false)
    axisRight.setDrawAxisLine(false)
    xAxis.setDrawAxisLine(false)
    xAxis.setDrawLabels(false)
    legend.isEnabled = false
    isDoubleTapToZoomEnabled = false
}