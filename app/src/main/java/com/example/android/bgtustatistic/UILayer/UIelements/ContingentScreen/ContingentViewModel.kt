package com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.ContingentRepository
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.MovementType
import com.example.android.bgtustatistic.DataLayer.UserManager.DataModels.User
import com.example.android.bgtustatistic.DataLayer.UserManager.UserManager
import com.example.android.bgtustatistic.UILayer.StateHolders.ContingentScreen.ContingentState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ContingentViewModel(
    private val repository: ContingentRepository
): ViewModel() {
    private val _uiState = MutableLiveData(ContingentState(contingentList = null, null, null))
    val uiState: LiveData<ContingentState> = _uiState

    private val fetchContingentJob: Job? = null
    fun fetchContingent(){
        fetchContingentJob?.cancel()
        viewModelScope.launch {
            val decreaseTypes = fetchDecreaseTypes()
            val increaseTypes = fetchIncreaseTypes()
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
                _uiState.value = ContingentState(
                    contingentList = response.body(),
                    decreaseTypes = decreaseTypes,
                    increaseTypes = increaseTypes)
            }
        }
    }

    private suspend fun fetchDecreaseTypes(): List<MovementType>{
        val user = UserManager.getUser()
        val response = try {
            repository.getDecreaseTypes("Bearer " + user.token!!)
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

    private var fetchITypesJob: Job? = null
    private suspend fun fetchIncreaseTypes(): List<MovementType>{
        fetchITypesJob?.cancel()
        val user = UserManager.getUser()
        val response = try {
            repository.getIncreaseTypes("Bearer " + user.token!!)
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

    override fun onCleared() {
        super.onCleared()
        fetchContingentJob?.cancel()
    }
}