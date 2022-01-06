package com.example.android.bgtustatistic.UILayer.StateHolders.LoginFeature

data class LoginState(
    val isLogin: Boolean = false,
    val isErrorOccurred: Boolean = false,
    val errorMessage: String? = null
)