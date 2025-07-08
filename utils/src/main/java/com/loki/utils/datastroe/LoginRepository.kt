package com.loki.utils.datastroe

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository private constructor(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "app_database.db"
    ).build()
    private val userLoginDao = db.userLoginDao()

    companion object {
        @Volatile private var instance: LoginRepository? = null
        fun getInstance(context: Context): LoginRepository =
            instance ?: synchronized(this) {
                instance ?: LoginRepository(context).also { instance = it }
            }
    }

    suspend fun saveCookie(cookie: String) = withContext(Dispatchers.IO) {
        userLoginDao.insertLogin(UserLoginEntity(cookie = cookie))
    }

    suspend fun getCookie(): String? = withContext(Dispatchers.IO) {
        userLoginDao.getLatestLogin()?.cookie
    }

    suspend fun clearCookie() = withContext(Dispatchers.IO) {
        userLoginDao.clearAll()
    }
} 