package com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRepository
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.ContingentMovement
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.MovementType
import com.example.android.bgtustatistic.DataLayer.LoginFeature.DataModels.UserInfo
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRepository
import com.example.android.bgtustatistic.DataLayer.UserManager.UserManager
import com.example.android.bgtustatistic.UILayer.StateHolders.ContingentScreen.ContingentState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ContingentViewModel(
    private val contingentRepository: ContingentRepository,
    private val loginRepository: LoginRepository
): ViewModel() {
    private val _uiState = MutableLiveData(ContingentState(false, contingentList = null, null, null, null))
    val uiState: LiveData<ContingentState> = _uiState
    var yearFilterValue = 2021
    var semesterFilterValue = SemesterFilter.all
    private var fetchContingentJob: Job? = null
    fun fetchContingent(){
        fetchContingentJob?.cancel()
        uiState.value?.contingentListFiltered?.let {
            if(!uiState.value?.relogined!!) {
                _uiState.value = uiState.value
                return
            }else{
                yearFilterValue = 2021
                semesterFilterValue = SemesterFilter.all
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
                _uiState.value = ContingentState(
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

    private var filterJob: Job? = null
    fun filterContingent(){
        filterJob?.cancel()
        filterJob = viewModelScope.launch {
            val result = emptyList<ContingentMovement>().toMutableList()

            uiState.value?.contingentList?.forEach { contingentMovement ->
                val listContingent = contingentMovement.contingent
                    .filter { (SemesterFilter.semesterFromMonthNumber(it.month) == semesterFilterValue
                            || semesterFilterValue == SemesterFilter.all) &&
                        it.year == yearFilterValue
                    }
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
                _uiState.value = ContingentState(
                    relogined = false,
                    contingentList = uiState.value?.contingentList,
                    contingentListFiltered = result,
                    decreaseTypes = uiState.value?.decreaseTypes,
                    increaseTypes = uiState.value?.increaseTypes,
                    noDataIsShowing = false,
                    firstStart = false
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
        _uiState.value = ContingentState(
            relogined = relogin,
            contingentList = uiState.value?.contingentList,
            contingentListFiltered = uiState.value?.contingentListFiltered,
            decreaseTypes = uiState.value?.decreaseTypes,
            increaseTypes = uiState.value?.increaseTypes,
            noDataIsShowing = uiState.value?.noDataIsShowing?:true,
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