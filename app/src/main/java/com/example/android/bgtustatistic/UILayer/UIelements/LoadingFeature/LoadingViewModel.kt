package com.example.android.bgtustatistic.UILayer.UIelements.LoadingFeature

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.bgtustatistic.DataLayer.UserManager.UserManager
import com.example.android.bgtustatistic.UILayer.StateHolders.LoadingFeature.LoadingState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoadingViewModel: ViewModel() {
    private val _isUserCashed = MutableLiveData(LoadingState())
    val isUserCashed: LiveData<LoadingState> = _isUserCashed

    private var loadingJob: Job? = null

    fun userIsCashed(){
        loadingJob?.cancel()
        viewModelScope.launch {
            val user = UserManager.getUser()
            _isUserCashed.value = LoadingState(
                isUserCashed = user.run { (username != null) && (password != null) && (token != null)  }
            )
            Log.e("loading: ", user.toString())
        }
    }

    override fun onCleared() {
        super.onCleared()
        loadingJob?.cancel()
    }
}