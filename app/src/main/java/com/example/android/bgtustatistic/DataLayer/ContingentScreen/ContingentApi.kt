package com.example.android.bgtustatistic.DataLayer.ContingentScreen

import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.ContingentMovement
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ContingentApi {
    @GET("contingent/chart_contingent")
    suspend fun getContingent(
        @Header("Authorization") token: String
    ): Response<List<ContingentMovement>>
}