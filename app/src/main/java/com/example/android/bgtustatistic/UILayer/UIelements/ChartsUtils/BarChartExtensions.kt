package com.example.android.bgtustatistic.UILayer.UIelements.ChartsUtils

import android.view.MotionEvent
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener

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

abstract class OnTouchReleaseListener(): OnChartGestureListener{
    override fun onChartGestureStart(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {}

    abstract override fun onChartGestureEnd(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    )

    override fun onChartLongPressed(me: MotionEvent?) {}
    override fun onChartDoubleTapped(me: MotionEvent?) {}
    override fun onChartSingleTapped(me: MotionEvent?) {}
    override fun onChartFling(
        me1: MotionEvent?,
        me2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ) {}
    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {}
    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {}
}

