package com.example.android.bgtustatistic.DataLayer.PerformanceScreen

import com.example.android.bgtustatistic.DataLayer.PerformanceScreen.DebtRemoteDataSource

class DebtRepository(
    private val dataSource: DebtRemoteDataSource
) {
    suspend fun getDebts(token: String) = dataSource.getDebts(token)
    suspend fun getDebtsById(token: String, id: Int) = dataSource.getDebtsById(token, id)
}