package com.example.android.bgtustatistic.UILayer.UIelements

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentApi
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRepository
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.Contingent
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginApi
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRepository
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtApi
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtRepository
import com.example.android.bgtustatistic.DataLayer.RetrofitBuilder.ServiceBuilder
import com.example.android.bgtustatistic.UILayer.PlotsAdapter
import com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen.ContingentViewModel
import com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen.ContingentViewModelFactory
import com.example.android.bgtustatistic.UILayer.UIelements.PerfromanceScreen.PerformanceViewModel
import com.example.android.bgtustatistic.UILayer.UIelements.PerfromanceScreen.PerformanceViewModelFactory
import com.example.android.bgtustatistic.databinding.FragmentInstitutesPlotsListBinding
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.Dispatchers

private const val ARG_PARAM1 = "recyclerType"

class InstitutesPlotsListFragment : Fragment() {
    private var recyclerType: RecyclerTypes? = null
    private var _binding : FragmentInstitutesPlotsListBinding? = null
    private lateinit var binding : FragmentInstitutesPlotsListBinding
    private val performanceViewModel: PerformanceViewModel by activityViewModels {
        PerformanceViewModelFactory(
            DebtRepository(
                dataSource = DebtRemoteDataSource(
                    debtApi = ServiceBuilder.buildService(DebtApi::class.java),
                    ioDispatcher = Dispatchers.IO
                )
            ),
            LoginRepository(loginRemoteDataSource = LoginRemoteDataSource(
                loginApi = ServiceBuilder.buildService(LoginApi::class.java),
                ioDispatcher = Dispatchers.IO
            ))

        )
    }
    private val contingentViewModel: ContingentViewModel by activityViewModels {
        ContingentViewModelFactory(
            ContingentRepository(
                dataSource = ContingentRemoteDataSource(
                    contingentApi = ServiceBuilder.buildService(ContingentApi::class.java),
                    ioDispatcher = Dispatchers.IO
                )
            ),
            LoginRepository(loginRemoteDataSource = LoginRemoteDataSource(
                loginApi = ServiceBuilder.buildService(LoginApi::class.java),
                ioDispatcher = Dispatchers.IO
            ))
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.getString(ARG_PARAM1)?.let { str ->
                recyclerType = RecyclerTypes.valueOf(str)
            }
        }
    }
    private fun generatePieDataSetEnrolled(): Array<PieDataSet>{
        val result = emptyList<PieDataSet>().toMutableList()
        contingentViewModel.uiState.value?.let {
            it.contingentList?.forEachIndexed{index, contingentMovement ->
                val entries = ArrayList<PieEntry>()
                contingentMovement.contingent.forEachIndexed { index, contingent ->
                    contingent.increasecontingent_set.forEachIndexed {index, increasecontingentSet ->
                        val label = contingentViewModel.uiState.value?.increaseTypes?.find {
                            it.id == increasecontingentSet.type
                        }
                        entries.add(
                            PieEntry(
                            increasecontingentSet.count_contingent.toFloat(),
                            label?.type_reason?:"Unknown",
                            increasecontingentSet)
                        )
                    }
                }
                if(entries.size != 0)
                    result.add(PieDataSet(entries, contingentMovement.short_name_department))
            }
        }
        Log.e("entries", result.size.toString())
        return Array(size = result.size) { i -> result[i] }
    }
    private fun generatePieDataSetDeducted(): Array<PieDataSet>{
        val result = emptyList<PieDataSet>().toMutableList()
        contingentViewModel.uiState.value?.let {
            it.contingentList?.forEachIndexed{index, contingentMovement ->
                val entries = ArrayList<PieEntry>()
                contingentMovement.contingent.forEachIndexed { index, contingent ->
                    contingent.decreasecontingent_set.forEachIndexed {index, increasecontingentSet ->
                        val label = contingentViewModel.uiState.value?.decreaseTypes?.find {
                            it.id == increasecontingentSet.type
                        }
                        entries.add(
                            PieEntry(
                                increasecontingentSet.count_contingent.toFloat(),
                                label?.type_reason?:"Unknown",
                                increasecontingentSet)
                        )
                    }
                }
                if(entries.size != 0)
                    result.add(PieDataSet(entries, contingentMovement.short_name_department))
            }
        }
        Log.e("entries", result.size.toString())
        return Array(size = result.size) { i -> result[i] }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstitutesPlotsListBinding.inflate(inflater)
        binding = _binding!!

        binding.plotsRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            if(recyclerType!! == RecyclerTypes.Enrolled)
                adapter = PlotsAdapter(generatePieDataSetEnrolled())
            if(recyclerType!! == RecyclerTypes.Deducted)
                adapter = PlotsAdapter(generatePieDataSetDeducted())
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: RecyclerTypes) =
            InstitutesPlotsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1.name)
                }
            }
    }
}