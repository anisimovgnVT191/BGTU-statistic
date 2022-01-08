package com.example.android.bgtustatistic.DataLayer.PerformanceScreen

import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DataModels.DepartmentDebt
import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DataModels.DepartmentDebtFull
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface DebtApi {
    @GET("depts/depts")
    suspend fun getDebts(
        @Header("Authorization") token: String
    ): Response<List<DepartmentDebt>>

    @GET("depts/depts/{id}/by_number_depts")
    suspend fun getDebtsById(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: Int
    ): Response<DepartmentDebtFull>

}