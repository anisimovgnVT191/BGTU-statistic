package com.example.android.bgtustatistic.UILayer.UIelements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.android.bgtustatistic.DataLayer.DebtApi
import com.example.android.bgtustatistic.DataLayer.DebtRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.DebtRepository
import com.example.android.bgtustatistic.DataLayer.DepartmentDebt
import com.example.android.bgtustatistic.DataLayer.RetrofitBuilder.ServiceBuilder
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.PerformanceViewModel
import com.example.android.bgtustatistic.UILayer.PerformanceViewModelFactory
import com.example.android.bgtustatistic.UILayer.StateHolders.RecyclerTypes
import com.example.android.bgtustatistic.databinding.FragmentPerformancePlotsBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.Dispatchers

class PerformancePlotsFragment : Fragment() {
    private var binding_ : FragmentPerformancePlotsBinding? = null
    private lateinit var binding : FragmentPerformancePlotsBinding
    private val viewModel: PerformanceViewModel by activityViewModels{
        PerformanceViewModelFactory(
            DebtRepository(
                dataSource = DebtRemoteDataSource(
                    debtApi = ServiceBuilder.buildService(DebtApi::class.java),
                    ioDispatcher = Dispatchers.IO
                )
            )
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding_ = FragmentPerformancePlotsBinding.inflate(inflater)
        binding = binding_!!

        binding.arrearsCard.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InstitutesPlotsFragment.newInstance(RecyclerTypes.Performance))
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observe(requireActivity()){ state ->
            state.debtsList?.let {
                setBarChartValues(it)
            }
        }
    }
    private fun setBarChartValues(list: List<DepartmentDebt>){
        val entries = ArrayList<BarEntry>()

        list.forEachIndexed { index, data ->
            entries.add(BarEntry(index.toFloat(), data.count_depts.toFloat()))
        }
        val dataSet = BarDataSet(entries, null)
        val barData = BarData(dataSet)
        binding.performanceBarchart.run {
            data = barData
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
        }
        binding.performanceBarchart.invalidate()
    }
    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }
}