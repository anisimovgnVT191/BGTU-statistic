package com.example.android.bgtustatistic.DataLayer.UserManager

import android.content.SharedPreferences
import com.example.android.bgtustatistic.DataLayer.UserManager.DataModels.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

private const val USERNAME_KEY = "username"
private const val PASSWORD_KEY = "password"
private const val TOKEN_KEY = "token"

class UserLocalDataSource(
    private val sharedPref: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getUser() =
        withContext(ioDispatcher){
            val username = sharedPref.getString(USERNAME_KEY, null)
            val password = sharedPref.getString(PASSWORD_KEY, null)
            val token = sharedPref.getString(TOKEN_KEY, null)
            User(username, password, token)
        }

    suspend fun updateUser(
        username: String,
        password: String,
    ){
        withContext(ioDispatcher){
            with(sharedPref.edit()){
                putString(USERNAME_KEY, username)
                putString(PASSWORD_KEY, password)
                commit()
            }
        }
    }

    suspend fun updateToken(
        token: String
    ){
        withContext(ioDispatcher){
            with(sharedPref.edit()){
                putString(TOKEN_KEY, token)
                commit()
            }
        }
    }

    suspend fun removeUser(){
        withContext(ioDispatcher){
            with(sharedPref.edit()){
                putString(USERNAME_KEY, null)
                putString(PASSWORD_KEY, null)
                putString(TOKEN_KEY, null)
                commit()
            }
        }
    }
}