package com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRepository
import com.example.android.bgtustatistic.DataLayer.UserManager.UserManager
import com.example.android.bgtustatistic.UILayer.StateHolders.ContingentScreen.PerformanceState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ContingentViewModel(
    private val repository: ContingentRepository
): ViewModel() {
    private val _uiState = MutableLiveData(PerformanceState(contingentList = null))
    val uiState: LiveData<PerformanceState> = _uiState

    private val fetchContingentJob: Job? = null
    fun fetchContingent(){
        fetchContingentJob?.cancel()
        viewModelScope.launch {
            val user = UserManager.getUser()
            val response = try {
                repository.getContingent("Bearer " + user.token!!)
            }catch (e: IOException){
                //internet error
                return@launch
            }catch (e: HttpException){
                //server error
                return@launch
            }
            if(response.isSuccessful){
                _uiState.value = PerformanceState(contingentList = response.body())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        fetchContingentJob?.cancel()
    }
}