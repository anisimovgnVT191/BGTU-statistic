package com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentApi
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRepository
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginApi
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRemoteDataSource
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRepository
import com.example.android.bgtustatistic.DataLayer.RetrofitBuilder.ServiceBuilder
import com.example.android.bgtustatistic.DataLayer.UserManager.UserManager
import com.example.android.bgtustatistic.R
import com.example.android.bgtustatistic.UILayer.UIelements.LoginFeature.LoginFragment
import com.example.android.bgtustatistic.databinding.FragmentSettingsBottomSheetBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SettingsBottomSheet: BottomSheetDialogFragment() {
    private var binding_: FragmentSettingsBottomSheetBinding? = null
    private lateinit var binding: FragmentSettingsBottomSheetBinding
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
    private val yearSpinnerList = (2021..Calendar.getInstance()[Calendar.YEAR]).toList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding_ = FragmentSettingsBottomSheetBinding.inflate(inflater)
        binding = binding_!!
        setYearSpinnerEntries(yearSpinnerList)
        binding.exitButton.setOnClickListener {
            requireActivity().run{
                lifecycleScope.launch {
                    UserManager.removeUser()
                }
                val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNav.visibility = View.GONE
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LoginFragment())
                    .commit()
            }
        }
        return binding.root
    }

    private fun setYearSpinnerEntries(list: List<Int>){
        binding.yearSpinner.adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_item,
            list.map { it.toString() }
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiStateSettings.distinctUntilChanged().observe(requireActivity()){state ->
            binding.semesterSpinner.setSelection(
                when(state.semesterFilter){
                    SemesterFilter.all -> 0
                    SemesterFilter.firstSemester -> 1
                    SemesterFilter.secondSemester -> 2
                }
            )
            binding.yearSpinner.setSelection(
                yearSpinnerList.indexOf(state.yearFilter)
            )
        }
    }
    override fun dismiss() {
        super.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.updateSettingsState(
            year = binding.yearSpinner.selectedItem.toString().toInt(),
            semester = when(binding.semesterSpinner.selectedItem.toString()){
                resources.getString(R.string.spinner_1_sem) -> SemesterFilter.firstSemester
                resources.getString(R.string.spinner_2_sem) -> SemesterFilter.secondSemester
                else -> SemesterFilter.all
            }
        )
        binding_ = null
    }
}