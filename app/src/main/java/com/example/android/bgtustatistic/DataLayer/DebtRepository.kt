package com.example.android.bgtustatistic.DataLayer

class DebtRepository(
    private val dataSource: DebtRemoteDataSource
) {
    suspend fun getDebts(token: String) = dataSource.getDebts(token)
    suspend fun getDebtsById(token: String, id: Int) = dataSource.getDebtsById(token, id)
}