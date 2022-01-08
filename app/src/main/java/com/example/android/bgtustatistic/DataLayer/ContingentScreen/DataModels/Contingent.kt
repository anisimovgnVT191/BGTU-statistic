package com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels

data class Contingent(
    val count_countingent_start_month: Int,
    val decreasecontingent_set: List<DecreasecontingentSet>,
    val department: Int,
    val id: Int,
    val increasecontingent_set: List<IncreasecontingentSet>,
    val month: Int,
    val year: Int
)