package com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentApi
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRepository
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.ContingentMovement
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DataModels.DepartmentDebt
import com.example.android.bgtustatistic.DataLayer.RetrofitBuilder.ServiceBuilder
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.CustomBarChartRender
import com.example.android.bgtustatistic.UILayer.UIelements.RecyclerTypes
import com.example.android.bgtustatistic.UILayer.UIelements.InstitutesPlotsFragment
import com.example.android.bgtustatistic.UILayer.makeOnlyBarsVisible
import com.example.android.bgtustatistic.databinding.FragmentContingentPlotsBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
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
            )
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContingentPlotsBinding.inflate(inflater)
        binding = _binding!!

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
                        InstitutesPlotsFragment.newInstance(RecyclerTypes.Enrolled)
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
        dataSet.setGradientColor(
            Color.parseColor(
                "#FFC5EC"
            ), Color.parseColor(
                "#FF79AF"
            ))
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

        binding.enrolledBarchart.run {
//            val barChartRender = CustomBarChartRender(
//                this, animator, viewPortHandler
//            )
//            barChartRender.setRadius(20)
//            renderer = barChartRender
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