package com.example.android.bgtustatistic.DataLayer.UserManager

import android.content.SharedPreferences
import com.example.android.bgtustatistic.DataLayer.UserManager.DataModels.User
import kotlinx.coroutines.CoroutineDispatcher

object UserManager {
    private lateinit var dataSource: UserLocalDataSource
    var currentUser: User? = null
        private set

    fun initialize(
        sharedPreferences: SharedPreferences,
        ioDispatcher: CoroutineDispatcher
    ){
        dataSource = UserLocalDataSource(sharedPreferences, ioDispatcher)
    }

    suspend fun getUser(): User {
        currentUser = dataSource.getUser()
        return currentUser!!
    }
    suspend fun updateUser(username: String, password: String) {
        dataSource.updateUser(username, password)
    }
    suspend fun updateToken(token: String) { dataSource.updateToken(token) }
    suspend fun removeUser() { dataSource.removeUser() }
}