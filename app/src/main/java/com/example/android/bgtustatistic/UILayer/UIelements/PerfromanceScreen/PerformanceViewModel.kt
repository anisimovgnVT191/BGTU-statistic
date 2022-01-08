package com.example.android.bgtustatistic.UILayer.UIelements.PerfromanceScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtRepository
import com.example.android.bgtustatistic.DataLayer.UserManager.UserManager
import com.example.android.bgtustatistic.UILayer.StateHolders.PerformanceScreen.PerformanceState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PerformanceViewModel(
    private val repository: DebtRepository
):ViewModel() {
    private val _uiState = MutableLiveData(PerformanceState())
    val uiState: LiveData<PerformanceState> = _uiState

    private var fetchDebtsJob: Job? = null
    fun fetchDebts(){
        fetchDebtsJob?.cancel()

        fetchDebtsJob = viewModelScope.launch {
            val user = UserManager.getUser()
            val response = try {
                repository.getDebts("Bearer " + user.token!!)
            }catch (e: IOException){
                //no internet
                return@launch
            }catch (e: HttpException){
                //pizda
                return@launch

            }
            if(response.isSuccessful){
                _uiState.value = PerformanceState(debtsList = response.body())
                Log.e("Performance", response.message() + response.code().toString())
            }
            else{
                Log.e("Performance", response.message() + response.code().toString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        fetchDebtsJob?.cancel()
    }
}