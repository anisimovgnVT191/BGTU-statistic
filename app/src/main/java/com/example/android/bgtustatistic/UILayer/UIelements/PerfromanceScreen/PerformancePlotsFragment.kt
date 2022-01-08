package com.example.android.bgtustatistic.UILayer.UIelements.PerfromanceScreen

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtApi
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtRepository
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DataModels.DepartmentDebt
import com.example.android.bgtustatistic.DataLayer.RetrofitBuilder.ServiceBuilder
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.UIelements.RecyclerTypes
import com.example.android.bgtustatistic.UILayer.UIelements.InstitutesPlotsFragment
import com.example.android.bgtustatistic.UILayer.makeOnlyBarsVisible
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
                .replace(R.id.fragment_container,
                    InstitutesPlotsFragment.newInstance(RecyclerTypes.Performance)
                )
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observe(requireActivity()){ state ->
            state.debtsList?.let {
                drawBarChart(it)
            }
        }
    }
    private fun drawBarChart(list: List<DepartmentDebt>){
        val entries = ArrayList<BarEntry>()

        list.forEachIndexed { index, data ->
            entries.add(BarEntry(index.toFloat(), data.count_depts.toFloat(), data))
        }
        val dataSet = BarDataSet(entries, null)
        dataSet.setGradientColor(
            Color.parseColor(
                "#FFC5EC"
            ), Color.parseColor(
                "#FF79AF"
            ))
        binding.performanceBarchart.run {
            data = BarData(dataSet)
            makeOnlyBarsVisible() //BarChartExtensions.kt
        }
        binding.performanceBarchart.invalidate()
    }
    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }
}