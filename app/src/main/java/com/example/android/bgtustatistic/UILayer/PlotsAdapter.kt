package com.example.android.bgtustatistic.UILayer

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FilterQueryProvider
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.bgtustatistic.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate

class PlotsAdapter(
    private val dataset: Array<PieDataSet>,
    private val pieChartColors: List<Int>
):RecyclerView.Adapter<PlotsAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val instShortName: TextView
        val pieChart: PieChart
        init {
            instShortName = view.findViewById(R.id.institute_short_name)
            pieChart = view.findViewById(R.id.institute_piechart)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pieDataSet = dataset[position]
        holder.instShortName.text = pieDataSet.label
        pieDataSet.label = null
        pieDataSet.colors = pieChartColors
        pieDataSet.valueTextColor = Color.WHITE
        pieDataSet.valueTextSize = 12F
        pieDataSet.valueFormatter = PercentFormatter(holder.pieChart)
        holder.pieChart.apply {
            data = PieData(pieDataSet)
            init() //PieChartExtensions.kt
            setOnChartValueSelectedListener(
                object : OnChartValueSelectedListener{
                    override fun onNothingSelected() {
                        centerText = ""
                    }

                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        val centerTextTmp = (e!! as PieEntry).label + "\n"
                        centerText = centerTextTmp + e.y.toInt().toString()
                    }
                }
            )
        }
    }

    override fun getItemCount() = dataset.size

}