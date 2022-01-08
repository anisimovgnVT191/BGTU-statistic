package com.example.android.bgtustatistic.DataLayer.ContingentScreen

class ContingentRepository(
    private val dataSource: ContingentRemoteDataSource
) {
    suspend fun getContingent(token: String) = dataSource.getContingent(token)
}