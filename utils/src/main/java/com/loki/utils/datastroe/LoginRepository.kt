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

    suspend fun saveUserInfo(cookie: String, userId: Long?, nickname: String?, avatarUrl: String?) = withContext(Dispatchers.IO) {
        userLoginDao.insertLogin(
            UserLoginEntity(
                cookie = cookie,
                userId = userId,
                nickname = nickname,
                avatarUrl = avatarUrl
            )
        )
    }

    suspend fun getLatestUserInfo(): UserLoginEntity? = withContext(Dispatchers.IO) {
        userLoginDao.getLatestLogin()
    }
} 