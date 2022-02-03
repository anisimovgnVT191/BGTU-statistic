package com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRepository
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.Contingent
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.ContingentMovement
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.MovementType
import com.example.android.bgtustatistic.DataLayer.LoginFeature.DataModels.UserInfo
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRepository
import com.example.android.bgtustatistic.DataLayer.UserManager.UserManager
import com.example.android.bgtustatistic.UILayer.StateHolders.ContingentScreen.ContingentState
import com.example.android.bgtustatistic.UILayer.StateHolders.ContingentScreen.SettingsState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class ContingentViewModel(
    private val contingentRepository: ContingentRepository,
    private val loginRepository: LoginRepository
): ViewModel() {
    private val _uiStateContingent = MutableLiveData(
        ContingentState(
            relogined = false,
            contingentList = null,
            contingentListFiltered = null,
            decreaseTypes = null,
            increaseTypes = null)
    )
    val uiStateContingent: LiveData<ContingentState> = _uiStateContingent

    private val _uiStateSettings = MutableLiveData(SettingsState())
    val uiStateSettings: LiveData<SettingsState> = _uiStateSettings

//    var yearFilterValue = Calendar.getInstance().get(Calendar.YEAR)
//    var semesterFilterValue = SemesterFilter.all

    fun updateSettingsState(year: Int, semester: SemesterFilter){
        _uiStateSettings.value = SettingsState(
            yearFilter = year,
            semesterFilter = semester
        )
    }
    private var fetchContingentJob: Job? = null
    fun fetchContingent(){
        fetchContingentJob?.cancel()
        uiStateContingent.value?.contingentListFiltered?.let {
            if(!uiStateContingent.value?.relogined!!) {
                _uiStateContingent.value = uiStateContingent.value
                return
            }else{
                _uiStateSettings.value = SettingsState()
            }
        }
        fetchContingentJob = viewModelScope.launch {
            val decreaseTypes = fetchDecreaseTypes()
            val increaseTypes = fetchIncreaseTypes()
            val user = UserManager.getUser()
            val response = try {
                contingentRepository.getContingent("Bearer " + user.token!!)
            }catch (e: IOException){
                //internet error
                return@launch
            }catch (e: HttpException){
                //server error
                return@launch
            }
            if(response.isSuccessful){
                _uiStateContingent.value = ContingentState(
                    relogined = false,
                    contingentList = response.body(),
                    contingentListFiltered = null,
                    decreaseTypes = decreaseTypes,
                    increaseTypes = increaseTypes,
                    noDataIsShowing = false,
                    firstStart = false)
            }
        }
    }
    private val filterYearSemester: (Contingent) -> Boolean = {contingent ->
        with(uiStateSettings.value!!){
            (SemesterFilter.semesterFromMonthNumber(contingent.month) == semesterFilter
                    || semesterFilter == SemesterFilter.all) &&
                    contingent.year == yearFilter
        }
    }
    private var filterJob: Job? = null
    fun filterContingent(){
        filterJob?.cancel()
        filterJob = viewModelScope.launch {
            val result = emptyList<ContingentMovement>().toMutableList()

            uiStateContingent.value?.contingentList?.forEach { contingentMovement ->
                val listContingent = contingentMovement.contingent
                    .filter(filterYearSemester)
                if(listContingent.isNotEmpty())
                    result.add(
                        ContingentMovement(
                            contingent = listContingent,
                            name_department = contingentMovement.name_department,
                            short_name_department = contingentMovement.short_name_department
                        )
                )
            }
            if (result.isNotEmpty())
                _uiStateContingent.value = uiStateContingent.value!!.copy(
                    relogined = false,
                    contingentListFiltered = result,
                    noDataIsShowing = false,
                    firstStart = false
                )
            else
                _uiStateContingent.value = uiStateContingent.value!!.copy(
                    contingentListFiltered = emptyList()
                )
        }
    }
    private var reloginJob: Job? = null
    fun updateToken(){
        reloginJob?.cancel()
        reloginJob = viewModelScope.launch {
            val user = UserManager.getUser()
            val response = try {
                loginRepository.login(UserInfo(password = user.password!!, username = user.username!!))
            }catch (e: IOException){
                return@launch
            }catch (e: HttpException){
                return@launch
            }
            if(response.isSuccessful && response.body()!=null){
                UserManager.updateToken(response.body()!!.token)
                updateStateByRelogin(true)
            }else{
                updateStateByRelogin(false)
            }
        }
    }

    private suspend fun fetchDecreaseTypes(): List<MovementType>{
        val user = UserManager.getUser()
        val response = try {
            contingentRepository.getDecreaseTypes("Bearer " + user.token!!)
        }catch (e: IOException){
            return emptyList()
        }catch (e: HttpException){
            return emptyList()
        }
        return if(response.isSuccessful && response.body() != null)
            response.body() as List<MovementType>
        else
            emptyList()
    }

    private suspend fun fetchIncreaseTypes(): List<MovementType>{
        val user = UserManager.getUser()
        val response = try {
            contingentRepository.getIncreaseTypes("Bearer " + user.token!!)
        }catch (e: IOException){
            return emptyList()
        }catch (e: HttpException){
            return emptyList()
        }
        return if(response.isSuccessful && response.body() != null)
            response.body() as List<MovementType>
        else
            emptyList()
    }

    private fun updateStateByRelogin(relogin: Boolean){
        _uiStateContingent.value = uiStateContingent.value!!.copy(
            relogined = relogin,
            contingentListFiltered = null,
            firstStart = false
        )
    }
    override fun onCleared() {
        super.onCleared()
        fetchContingentJob?.cancel()
        reloginJob?.cancel()
        filterJob?.cancel()
    }
}