package com.example.android.bgtustatistic.UILayer.UIelements.PerfromanceScreen

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginApi
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRepository
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtApi
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtRepository
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DataModels.DepartmentDebt
import com.example.android.bgtustatistic.DataLayer.RetrofitBuilder.ServiceBuilder
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.UIelements.ChartsUtils.OnTouchReleaseListener
import com.example.android.bgtustatistic.UILayer.UIelements.ChartsUtils.makeOnlyBarsVisible
import com.example.android.bgtustatistic.UILayer.UIelements.InstitutiesPieChartsScreen.InstitutesPlotsFragment
import com.example.android.bgtustatistic.UILayer.UIelements.InstitutiesPieChartsScreen.RecyclerTypes
import com.example.android.bgtustatistic.databinding.FragmentPerformancePlotsBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.textview.MaterialTextView
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
            ),
            LoginRepository(
                loginRemoteDataSource = LoginRemoteDataSource(
                    loginApi = ServiceBuilder.buildService(LoginApi::class.java),
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

        initBarChartLayout()

        binding.arrearsCard.setOnClickListener {
            viewModel.fetchDebtsById(
                viewModel.uiState.value!!.debtsList!!.filter { it.count_depts != 0 }.map { it.id }
            )
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
    inner class OnTouchReleaseListenerImpl(
        private val titleText: String,
        private val shortNameTextView: MaterialTextView,
        private val titleTextView: MaterialTextView,
        private val barChart: BarChart
    ): OnTouchReleaseListener(){
        override fun onChartGestureEnd(
            me: MotionEvent?,
            lastPerformedGesture: ChartTouchListener.ChartGesture?
        ) {
            me?.let {
                if(it.action == MotionEvent.ACTION_UP || it.action == MotionEvent.ACTION_CANCEL){
                    binding.apply {
                        shortNameTextView.text = ""
                        titleTextView.text = titleText
                        barChart.isSelected = false
                        barChart.highlightValues(null)
                    }
                }
            }
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun initBarChartLayout(){
        binding.performanceBarchart.apply {
            setOnChartValueSelectedListener(
                object : OnChartValueSelectedListener{
                    override fun onNothingSelected() {
                        binding.arrearsTextview.text = getString(R.string.arrears)
                        binding.perfInstShorName.text = ""
                    }

                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        binding.arrearsTextview.text = e?.y?.toInt().toString()
                        binding.perfInstShorName.text = (e?.data as DepartmentDebt).short_name_department
                    }
                }
            )
            onChartGestureListener = OnTouchReleaseListenerImpl(
                titleText = getString(R.string.arrears),
                shortNameTextView = binding.perfInstShorName,
                titleTextView = binding.arrearsTextview,
                barChart = this
            )
        }
    }
    private fun drawBarChart(list: List<DepartmentDebt>){
        val entries = ArrayList<BarEntry>()

        list.filter { it.count_depts != 0 }.forEachIndexed{index, data ->
            entries.add(BarEntry(index.toFloat(), data.count_depts.toFloat(), data))
        }

        val dataSet = BarDataSet(entries, null)
        dataSet.setGradientColor(
            Color.parseColor(
                "#FFC5EC"
            ), Color.parseColor(
                "#FF79AF"
            ))
        dataSet.setDrawValues(false)
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