package com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentApi
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRepository
import com.example.android.bgtustatistic.DataLayer.RetrofitBuilder.ServiceBuilder
import com.example.android.bgtustatistic.UILayer.UIelements.NoDataFragment
import com.example.android.bgtustatistic.databinding.FragmentMovementBinding
import kotlinx.coroutines.Dispatchers

class ContingentFragment : Fragment() {
    private var _binding: FragmentMovementBinding? = null
    private lateinit var binding: FragmentMovementBinding
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
        _binding = FragmentMovementBinding.inflate(inflater)
        binding = _binding!!
        viewModel.fetchContingent()
        binding.apply {
            updateMovButton.setOnClickListener {
                childFragmentManager.beginTransaction()
                    .replace(binding.movContainer.id, NoDataFragment())
                    .commit()
            }
            settingsButton.setOnClickListener {
                showSettingsBottomSheet()
            }
        }

        childFragmentManager.beginTransaction()
            .replace(binding.movContainer.id, ContingentPlotsFragment())
            .commit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observe(requireActivity()){ state ->
            state.contingentList?.let {
                Log.e("Contingent: ", it.toString())
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun showSettingsBottomSheet(){
        val bottomSheet = SettingsBottomSheet()

        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

}