package com.example.android.bgtustatistic.DataLayer.ContingentScreen

class ContingentRepository(
    private val dataSource: ContingentRemoteDataSource
) {
    suspend fun getContingent(token: String) = dataSource.getContingent(token)
    suspend fun getDecreaseTypes(token: String) = dataSource.getDecreaseTypes(token)
    suspend fun getIncreaseTypes(token: String) = dataSource.getIncreaseTypes(token)
}