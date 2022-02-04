package com.example.android.bgtustatistic.DataLayer.ContingentScreen.DataModels

data class DecreasecontingentSet(
    override val contingent: Int,
    override val count_contingent: Int,
    override val id: Int,
    override val percent: Double,
    override val type: Int
):MovementSet