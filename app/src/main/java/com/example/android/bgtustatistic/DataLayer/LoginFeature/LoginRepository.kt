package com.example.android.bgtustatistic.DataLayer.LoginFeature

import com.example.android.bgtustatistic.DataLayer.LoginFeature.DataModels.Token
import com.example.android.bgtustatistic.DataLayer.LoginFeature.DataModels.UserInfo
import retrofit2.Response

class LoginRepository(
    private val loginRemoteDataSource: LoginRemoteDataSource
) {
    suspend fun login(userInfo: UserInfo): Response<Token> = loginRemoteDataSource.login(userInfo)
    suspend fun refresh(token: Token): Response<Token> = loginRemoteDataSource.refresh(token)
    suspend fun verify(token: Token): Response<Token> = loginRemoteDataSource.verify(token)
}