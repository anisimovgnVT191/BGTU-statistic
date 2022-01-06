package com.example.android.bgtustatistic.DataLayer.LoginFeature

import com.example.android.bgtustatistic.DataLayer.LoginFeature.DataModels.Token
import com.example.android.bgtustatistic.DataLayer.LoginFeature.DataModels.UserInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginRemoteDataSource(
    private val loginApi: LoginApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun login(userInfo: UserInfo): Response<Token> =
        withContext(ioDispatcher){
            loginApi.login(userInfo)
        }
    suspend fun refresh(token: Token): Response<Token> =
        withContext(ioDispatcher){
            loginApi.refresh(token)
        }
    suspend fun verify(token: Token): Response<Token> =
        withContext(ioDispatcher){
            loginApi.verify(token)
        }


}