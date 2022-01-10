package com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding_ = FragmentSettingsBottomSheetBinding.inflate(inflater)
        binding = binding_!!

        binding.semesterSpinner.setSelection(
            when(viewModel.semesterFilterValue){
                SemesterFilter.all -> 0
                SemesterFilter.firstSemester -> 1
                SemesterFilter.secondSemester -> 2
            }
        )
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

    override fun dismiss() {
        super.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.semesterFilterValue = when(binding.semesterSpinner.selectedItem.toString()){
            resources.getString(R.string.spinner_1_sem) -> SemesterFilter.firstSemester
            resources.getString(R.string.spinner_2_sem) -> SemesterFilter.secondSemester
            else -> SemesterFilter.all
        }
        viewModel.yearFilterValue = binding.yearSpinner.selectedItem.toString().toIntOrNull()?:2021
        viewModel.filterContingent()
        binding_ = null
    }
}