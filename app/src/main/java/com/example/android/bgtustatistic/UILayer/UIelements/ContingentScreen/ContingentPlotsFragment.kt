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
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentApi
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRepository
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.ContingentMovement
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginApi
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRepository
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DataModels.DepartmentDebt
import com.example.android.bgtustatistic.DataLayer.RetrofitBuilder.ServiceBuilder
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.CustomBarChartRender
import com.example.android.bgtustatistic.UILayer.OnTouchReleaseListener
import com.example.android.bgtustatistic.UILayer.UIelements.RecyclerTypes
import com.example.android.bgtustatistic.UILayer.UIelements.InstitutesPlotsFragment
import com.example.android.bgtustatistic.UILayer.makeOnlyBarsVisible
import com.example.android.bgtustatistic.databinding.FragmentContingentPlotsBinding
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.coroutines.Dispatchers

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
                    .replace(R.id.fragment_container,
                        InstitutesPlotsFragment.newInstance(RecyclerTypes.Deducted)
                    )
                    .addToBackStack(null)
                    .commit()
            }
            enrolledDetailsButton.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                        InstitutesPlotsFragment.newInstance(
                            RecyclerTypes.Enrolled,
                            )
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observe(requireActivity()){ state ->
            state.contingentList?.let {
                drawBarChartDeducted(it)
                drawBarCharEnrolled(it)
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

        list.forEachIndexed { index, data ->
            val deducted = data.contingent.foldRight(0.0){contingent, acc ->
                contingent.decreasecontingent_set.sumOf { it.percent } + acc
            }
           if(deducted != 0.0)
                entries.add(BarEntry(index.toFloat(), deducted.toFloat(), data))
        }
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

        list.forEachIndexed { index, data ->
            val deducted = data.contingent.foldRight(0.0){contingent, acc ->
                contingent.increasecontingent_set.sumOf { it.percent } + acc
            }
            if(deducted != 0.0)
                entries.add(BarEntry(index.toFloat(), deducted.toFloat(), data))
        }
        val dataSet = BarDataSet(entries, null)
        dataSet.setGradientColor(
            Color.parseColor(
                "#FFC5EC"
            ), Color.parseColor(
                "#FF79AF"
            ))
        dataSet.setDrawValues(false)
        binding.enrolledBarchart.run {
            data = BarData(dataSet)
            makeOnlyBarsVisible() //BarChartExtensions.kt
        }
        binding.enrolledBarchart.invalidate()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}