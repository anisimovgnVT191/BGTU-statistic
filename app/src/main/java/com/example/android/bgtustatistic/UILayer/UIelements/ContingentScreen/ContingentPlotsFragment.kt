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
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.ContingentMovement
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
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
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
                drawBarChartDeducted(it)
                drawBarCharEnrolled(it)
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
    private fun initBarCharts(){
        binding.deductedBarchart.apply {
            setOnChartValueSelectedListener(
                object : OnChartValueSelectedListener{
                    override fun onNothingSelected() {
                        binding.deductedTextview.text = getString(R.string.deducted)
                        binding.deductedInsShortName.text = ""
                    }

                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        binding.deductedInsShortName.text = (e?.data as ContingentMovement).short_name_department
                        binding.deductedTextview.text = e.y.toString()
                    }
                }
            )
            onChartGestureListener = OnTouchReleaseListener { me, _ ->
                me?.let {
                    if(it.action == MotionEvent.ACTION_UP || it.action == MotionEvent.ACTION_CANCEL){
                        binding.apply {
                            deductedInsShortName.text = ""
                            deductedTextview.text = getString(R.string.arrears)
                            isSelected = false
                            highlightValues(null)
                        }
                    }
                }
            }
        }
        binding.enrolledBarchart.apply {
            setOnChartValueSelectedListener(
                object : OnChartValueSelectedListener{
                    override fun onNothingSelected() {
                        binding.enrolledTextview.text = getString(R.string.deducted)
                        binding.enrolledShortName.text = ""
                    }

                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        binding.enrolledShortName.text = (e?.data as ContingentMovement).short_name_department
                        binding.enrolledTextview.text = e.y.toString()
                    }
                }
            )
            onChartGestureListener = OnTouchReleaseListener { me, _ ->
                me?.let {
                    if(it.action == MotionEvent.ACTION_UP || it.action == MotionEvent.ACTION_CANCEL){
                        binding.apply {
                            enrolledShortName.text = ""
                            enrolledTextview.text = getString(R.string.arrears)
                            isSelected = false
                            highlightValues(null)
                        }
                    }
                }
            }
        }
    }
    private fun drawBarChartDeducted(list: List<ContingentMovement>){
        val entries = ArrayList<BarEntry>()
        var xIndex = 0;
        list.forEachIndexed { index, data ->
            val deducted = data.contingent.foldRight(0.0){contingent, acc ->
                contingent.decreasecontingent_set.sumOf { it.percent } + acc
            }
           if(deducted != 0.0) {
               ++xIndex
               entries.add(BarEntry(xIndex.toFloat(), deducted.toFloat().roundToOneDecimal(), data))
           }

        }
        Log.e("drawBarCharDeducted", entries.size.toString())
        val dataSet = BarDataSet(entries, null)
        dataSet.setGradientColor(Color.parseColor("#FFC5EC"), Color.parseColor("#FF79AF"))
        dataSet.setDrawValues(false)
        binding.deductedBarchart.run {
            data = BarData(dataSet)
            makeOnlyBarsVisible() //BarChartExtensions.kt
        }
        binding.deductedBarchart.invalidate()
    }
    private fun drawBarCharEnrolled(list: List<ContingentMovement>){
        val entries = ArrayList<BarEntry>()
        var xIndex = 0;
        list.forEachIndexed { index, data ->
            val enrolled = data.contingent.foldRight(0.0){ contingent, acc ->
                contingent.increasecontingent_set.sumOf { it.percent } + acc
            }
            if(enrolled != 0.0){
                xIndex++;
                entries.add(BarEntry(xIndex.toFloat(), enrolled.toFloat().roundToOneDecimal(), data))
            }
        }
        Log.e("drawBarCharEnrolled", entries.size.toString())
        val dataSet = BarDataSet(entries, null)
        dataSet.setGradientColor(
            Color.parseColor(
                "#7CCBED"
            ), Color.parseColor(
                "#21A4E2"
            ))
        dataSet.setDrawValues(false)
        binding.enrolledBarchart.run {
            data = BarData(dataSet)
            makeOnlyBarsVisible() //BarChartExtensions.kt
        }
        binding.enrolledBarchart.invalidate()
    }
    private fun Float.roundToOneDecimal() =
        this.toBigDecimal().setScale(1, RoundingMode.UP).toFloat()
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}