package com.example.android.bgtustatistic.UILayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.bgtustatistic.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet

class PlotsAdapter(
    private val dataset: Array<PieDataSet>
):RecyclerView.Adapter<PlotsAdapter.ViewHolder>() {
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
        holder.pieChart.apply {
            data = PieData(pieDataSet)
            description = null
        }
    }

    override fun getItemCount() = dataset.size
}