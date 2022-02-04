package com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.distinctUntilChanged
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentApi
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRepository
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.Contingent
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.ContingentMovement
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.MovementSet
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginApi
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRepository
import com.example.android.bgtustatistic.DataLayer.RetrofitBuilder.ServiceBuilder
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.UIelements.ChartsUtils.OnTouchReleaseListener
import com.example.android.bgtustatistic.UILayer.UIelements.InstitutiesPieChartsScreen.RecyclerTypes
import com.example.android.bgtustatistic.UILayer.UIelements.InstitutiesPieChartsScreen.InstitutesPlotsFragment
import com.example.android.bgtustatistic.UILayer.UIelements.ChartsUtils.makeOnlyBarsVisible
import com.example.android.bgtustatistic.databinding.FragmentContingentPlotsBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Dispatchers
import java.math.RoundingMode

class ContingentPlotsFragment : Fragment() {
    private var _binding : FragmentContingentPlotsBinding? = null
    private lateinit var binding : FragmentContingentPlotsBinding
    private val viewModel: ContingentViewModel by activityViewModels {
        ContingentViewModelFactory(
            ContingentRepository(
                dataSource = ContingentRemoteDataSource(
                    contingentApi = ServiceBuilder.buildService(ContingentApi::class.java),
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
        _binding = FragmentContingentPlotsBinding.inflate(inflater)
        binding = _binding!!

        initBarCharts()
        binding.apply {
            deductedDetailsButton.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        InstitutesPlotsFragment.newInstance(RecyclerTypes.Deducted))
                    .addToBackStack(null)
                    .commit()

            }
            enrolledDetailsButton.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                        InstitutesPlotsFragment.newInstance(RecyclerTypes.Enrolled,))
                    .addToBackStack(null)
                    .commit()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiStateContingent.distinctUntilChanged().observe(requireActivity()){ state ->
            with(state){
                if(contingentList!=null && contingentListFiltered==null)
                    viewModel.filterContingent()
            }
            state.contingentListFiltered?.let {
                drawBarChart(
                    list = it,
                    gradientColors = Pair(
                        first = Color.parseColor("#FFC5EC"),
                        second = Color.parseColor("#FF79AF")),
                    barChart = binding.deductedBarchart
                ){ contingent ->  contingent.decreasecontingent_set }
                drawBarChart(
                    list = it,
                    gradientColors = Pair(
                        first = Color.parseColor("#7CCBED"),
                        second =  Color.parseColor("#21A4E2")
                    ),
                    barChart = binding.enrolledBarchart
                ) { contingent -> contingent.increasecontingent_set }
            }
        }
        viewModel.uiStateSettings.distinctUntilChanged().observe(requireActivity()){state ->
            if(!isAdded) return@observe
            binding.yearText.text = "${state.yearFilter} ${requireActivity().resources.getString(R.string.year)}"
            binding.semesterText.text = when(state.semesterFilter){
                SemesterFilter.all -> ""
                SemesterFilter.firstSemester -> requireActivity().resources.getString(R.string.spinner_1_sem)
                SemesterFilter.secondSemester -> requireActivity().resources.getString(R.string.spinner_2_sem)
            }
        }
    }
    inner class OnChartValueSelectedListenerImpl(
        private val titleText: String,
        private val shortNameTextView: MaterialTextView,
        private val titleTextView: MaterialTextView
    ):OnChartValueSelectedListener{
        override fun onValueSelected(e: Entry?, h: Highlight?) {
            shortNameTextView.text = (e?.data as ContingentMovement).short_name_department
            titleTextView.text = e.y.toString()
        }

        override fun onNothingSelected() {
            titleTextView.text = titleText
            shortNameTextView.text = ""
        }

    }
    private fun initBarCharts(){
        binding.deductedBarchart.apply {
            setOnChartValueSelectedListener(
                OnChartValueSelectedListenerImpl(
                    titleText = getString(R.string.deducted),
                    shortNameTextView = binding.deductedInsShortName,
                    titleTextView = binding.deductedTextview))
            onChartGestureListener = OnTouchReleaseListener { me, _ ->
                me?.let {
                    if(it.action == MotionEvent.ACTION_UP || it.action == MotionEvent.ACTION_CANCEL){
                        binding.apply {
                            deductedInsShortName.text = ""
                            deductedTextview.text = getString(R.string.deducted)
                            isSelected = false
                            highlightValues(null)
                        }
                    }
                }
            }
        }
        binding.enrolledBarchart.apply {
            setOnChartValueSelectedListener(
                OnChartValueSelectedListenerImpl(
                    titleText = getString(R.string.deducted),
                    shortNameTextView = binding.enrolledShortName,
                    titleTextView = binding.enrolledTextview))
            onChartGestureListener = OnTouchReleaseListener { me, _ ->
                me?.let {
                    if(it.action == MotionEvent.ACTION_UP || it.action == MotionEvent.ACTION_CANCEL){
                        binding.apply {
                            enrolledShortName.text = ""
                            enrolledTextview.text = getString(R.string.enrolled)
                            isSelected = false
                            highlightValues(null)
                        }
                    }
                }
            }
        }
    }
    private fun drawBarChart(
        list: List<ContingentMovement>,
        gradientColors: Pair<Int, Int>,
        barChart: BarChart,
        takeMovementSet: (Contingent) -> List<MovementSet>
    ){
        val entries = ArrayList<BarEntry>()
        var xIndex = 0;
        list.forEachIndexed { index, data ->
            val movement = data.contingent.foldRight(0.0){ contingent, acc ->
                takeMovementSet(contingent).sumOf { it.percent } + acc
            }
            if(movement != 0.0){
                xIndex++;
                entries.add(BarEntry(xIndex.toFloat(), movement.toFloat().roundToOneDecimal(), data))
            }
        }
        Log.e("drawBarCharEnrolled", entries.size.toString())
        val dataSet = BarDataSet(entries, null)
        dataSet.setGradientColor(gradientColors.first, gradientColors.second)
        dataSet.setDrawValues(false)
        barChart.run {
            data = BarData(dataSet)
            makeOnlyBarsVisible() //BarChartExtensions.kt
        }
        barChart.invalidate()
    }
    private fun Float.roundToOneDecimal() =
        this.toBigDecimal().setScale(1, RoundingMode.UP).toFloat()
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}