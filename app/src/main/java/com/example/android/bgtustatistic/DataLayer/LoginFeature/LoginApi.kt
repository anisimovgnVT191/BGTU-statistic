package com.example.android.bgtustatistic.DataLayer.LoginFeature

import com.example.android.bgtustatistic.DataLayer.LoginFeature.DataModels.Token
import com.example.android.bgtustatistic.DataLayer.LoginFeature.DataModels.UserInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("token/auth/")
    suspend fun login(@Body userInfo: UserInfo): Response<Token>

    @POST("token/verify/")
    suspend fun verify(@Body token: Token): Response<Token>

    @POST("token/refresh/")
    suspend fun refresh(@Body token: Token): Response<Token>
}