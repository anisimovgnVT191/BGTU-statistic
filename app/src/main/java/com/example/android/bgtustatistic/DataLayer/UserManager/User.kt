package com.example.android.bgtustatistic.DataLayer.UserManager

data class User(
    val username: String?,
    val password: String?,
    val token: String?
){
    fun isValid() = (username!=null) && (password!=null) && (token!=null)
}