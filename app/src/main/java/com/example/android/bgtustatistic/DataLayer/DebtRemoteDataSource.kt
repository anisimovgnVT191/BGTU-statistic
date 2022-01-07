package com.example.android.bgtustatistic.DataLayer

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DebtRemoteDataSource(
    private val debtApi: DebtApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getDebts(token: String) =
        withContext(ioDispatcher){
            debtApi.getDebts(token)
        }

    suspend fun getDebtsById(token: String, id: Int) =
        withContext(ioDispatcher){
            debtApi.getDebtsById(token, id)
        }
}