package com.example.android.bgtustatistic.UILayer.UIelements.PerfromanceScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtApi
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtRepository
import com.example.android.bgtustatistic.DataLayer.RetrofitBuilder.ServiceBuilder
import com.example.android.bgtustatistic.UILayer.UIelements.NoDataFragment
import com.example.android.bgtustatistic.databinding.FragmentPerformanceBinding
import kotlinx.coroutines.Dispatchers

class PerformanceFragment : Fragment() {
    private var binding_: FragmentPerformanceBinding? = null
    private lateinit var binding: FragmentPerformanceBinding
    private val viewModel: PerformanceViewModel by activityViewModels {
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
        binding_ = FragmentPerformanceBinding.inflate(inflater)
        binding = binding_!!
        viewModel.fetchDebts()
        binding.updatePerfButton.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(binding.perfContainer.id, NoDataFragment())
                .commit()
        }
        childFragmentManager.beginTransaction()
            .replace(binding.perfContainer.id, PerformancePlotsFragment())
            .commit()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observe(requireActivity()){ state ->
            state.debtsList?.let {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }
}