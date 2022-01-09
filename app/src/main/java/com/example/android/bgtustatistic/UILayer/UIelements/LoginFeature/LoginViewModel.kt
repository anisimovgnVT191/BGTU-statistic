package com.example.android.bgtustatistic.UILayer.UIelements.LoginFeature

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.bgtustatistic.DataLayer.LoginFeature.LoginRepository
import com.example.android.bgtustatistic.DataLayer.LoginFeature.DataModels.UserInfo
import com.example.android.bgtustatistic.DataLayer.UserManager.UserManager
import com.example.android.bgtustatistic.UILayer.StateHolders.LoginFeature.LoginState
import com.example.android.bgtustatistic.UILayer.UIelements.isValid
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(
    private val repository: LoginRepository
): ViewModel() {
    private val _uiState = MutableLiveData(LoginState())
    val uiState: LiveData<LoginState> = _uiState

    private var loginJob: Job? = null

    fun login(username: String, password: String) {
        loginJob?.cancel()
        if(!username.isValid() and !password.isValid()){
            _uiState.value = LoginState(
                isErrorOccurred = true,
                errorType = LoginErrorType.EmptyFields)
            return
        }
        loginJob = viewModelScope.launch {
            val response = try {
                repository.login(
                    UserInfo(
                    username = username,
                    password = password
                )
                )
            }catch (e: IOException){
                Log.e("login", e.message + e.localizedMessage)
                _uiState.value = LoginState(
                    isErrorOccurred = true,
                    errorType = LoginErrorType.NoInternetConnection)
                return@launch
            }catch (e: HttpException){
                _uiState.value = LoginState(
                    isErrorOccurred = true,
                    errorType = LoginErrorType.WrongCredentials)
                return@launch
            }

            if(response.isSuccessful && response.body()!=null) {
                _uiState.value = LoginState(isLogin = true)
                UserManager.run {
                    updateToken(response.body()!!.token)
                    updateUser(username, password)
                }
            }
            else
                when(response.code()){
                    400 -> _uiState.value = LoginState(
                        isErrorOccurred = true,
                        errorType = LoginErrorType.WrongCredentials)
                    else -> _uiState.value = LoginState(
                        isErrorOccurred = true,
                        errorType = LoginErrorType.ServerSideError)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        loginJob?.cancel()
    }
}