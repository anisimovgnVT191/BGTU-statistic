package com.example.android.bgtustatistic.UILayer.UIelements.PerfromanceScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.bgtustatistic.DataLayer.LoginFeature.DataModels.UserInfo
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRepository
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DataModels.DepartmentDebtFull
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtRepository
import com.example.android.bgtustatistic.DataLayer.UserManager.UserManager
import com.example.android.bgtustatistic.UILayer.StateHolders.PerformanceScreen.PerformanceState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PerformanceViewModel(
    private val debtRepository: DebtRepository,
    private val loginRepository: LoginRepository
):ViewModel() {
    private val _uiState = MutableLiveData(PerformanceState(false))
    val uiState: LiveData<PerformanceState> = _uiState

    private var fetchDebtsJob: Job? = null
    fun fetchDebts(){
        fetchDebtsJob?.cancel()

        fetchDebtsJob = viewModelScope.launch {
            val user = UserManager.getUser()
            val response = try {
                debtRepository.getDebts("Bearer " + user.token!!)
            }catch (e: IOException){
                //no internet
                return@launch
            }catch (e: HttpException){

                return@launch

            }
            if(response.isSuccessful){
                _uiState.value = PerformanceState(
                    relogined = false,
                    debtsList = response.body(),
                    noDataIsShowing = false)
                Log.e("Performance", response.message() + response.code().toString())
            }
            else{
                Log.e("Performance", response.message() + response.code().toString())
            }
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

    private var fetchDebtsByIdJob: Job? = null
    fun fetchDebtsById(idsList: List<Int>){
        fetchDebtsByIdJob?.cancel()
        fetchDebtsByIdJob = viewModelScope.launch {
            val debtsList: MutableList<DepartmentDebtFull> = emptyList<DepartmentDebtFull>()
                .toMutableList()
            val token = UserManager.getUser().token
            idsList.forEach { id ->
                val debtsItem = fetchDebtById(id, token!!)
                debtsItem?.let {
                    debtsList.add(debtsItem)
                }
            }
            if(debtsList.isNotEmpty()){
                _uiState.value = uiState.value!!.let {
                    PerformanceState(
                        relogined = it.relogined,
                        debtsList = it.debtsList,
                        noDataIsShowing = it.noDataIsShowing,
                        debtsByIdList = debtsList
                    )
                }
            }
        }
    }

    private suspend fun fetchDebtById(id: Int, token: String): DepartmentDebtFull?{
        val requestResult = try {
            debtRepository.getDebtsById(token = "Bearer $token", id = id)
        }catch (e: HttpException){
            return null
        }catch (e: IOException){
            return null
        }
        return requestResult.body()
    }
    private fun updateStateByRelogin(relogin: Boolean){
        _uiState.value = PerformanceState(
            relogined = relogin,
            debtsList = uiState.value?.debtsList,
            noDataIsShowing = uiState.value?.noDataIsShowing?:true
        )
    }
    override fun onCleared() {
        super.onCleared()
        fetchDebtsJob?.cancel()
        reloginJob?.cancel()
        fetchDebtsByIdJob?.cancel()
    }
}