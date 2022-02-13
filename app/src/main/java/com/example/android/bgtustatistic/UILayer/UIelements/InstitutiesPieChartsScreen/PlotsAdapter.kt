package com.example.android.bgtustatistic.UILayer.UIelements.InstitutiesPieChartsScreen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.UIelements.ChartsUtils.PieChartValuesFormatter
import com.example.android.bgtustatistic.UILayer.init
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

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
        pieDataSet.colors = pieChartColors
        pieDataSet.valueTextColor = Color.WHITE
        pieDataSet.valueTextSize = 12F
        pieDataSet.valueFormatter = PieChartValuesFormatter()
        holder.pieChart.apply {
            data = PieData(pieDataSet)
            centerText = """${resources.getString(R.string.pie_chart_center_text)}
                |
                |${data.yValueSum.toInt()}
            """.trimMargin()
            init() //PieChartExtensions.kt
            setOnChartValueSelectedListener(
                object : OnChartValueSelectedListener{
                    override fun onNothingSelected() {
                        centerText = """${resources.getString(R.string.pie_chart_center_text)}
                            |
                            |${data.yValueSum.toInt()}
                        """.trimMargin()
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