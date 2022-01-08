package com.example.android.bgtustatistic.DataLayer.ContingentScreen

import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.ContingentMovement
import com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels.MovementType
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ContingentApi {
    @GET("contingent/chart_contingent")
    suspend fun getContingent(
        @Header("Authorization") token: String
    ): Response<List<ContingentMovement>>

    @GET("dict/dict_type_decrease")
    suspend fun getDecreaseTypes(
        @Header("Authorization") token: String
    ): Response<List<MovementType>>

    @GET("dict/dict_type_increase")
    suspend fun getIncreaseTypes(
        @Header("Authorization") token: String
    ): Response<List<MovementType>>
}