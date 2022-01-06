package com.example.android.bgtustatistic.UILayer.StateHolders.LoginFeature

import com.example.android.bgtustatistic.UILayer.UIelements.LoginFeature.LoginErrorType

data class LoginState(
    val isLogin: Boolean = false,
    val isErrorOccurred: Boolean = false,
    val errorType: LoginErrorType? = null
)