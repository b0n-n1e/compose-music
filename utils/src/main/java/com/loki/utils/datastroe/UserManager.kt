package com.loki.utils.datastroe

import android.content.Context
import android.util.Log
import com.loki.utils.extension.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UserInfo(
    val userId: Long?,
    val nickname: String?,
    val avatarUrl: String?
)

object UserManager {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _cookie = MutableStateFlow<String?>(null)
    val cookie: StateFlow<String?> = _cookie.asStateFlow()

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo.asStateFlow()

    private lateinit var repository: LoginRepository
    private var initialized = false

    fun init(context: Context) {
        if (!initialized) {
            repository = LoginRepository.getInstance(context)
            CoroutineScope(Dispatchers.IO).launch {
                val saved = repository.getLatestUserInfo()
                _cookie.value = saved?.cookie
                _isLoggedIn.value = !saved?.cookie.isNullOrEmpty()
                _userInfo.value = if (saved != null && !saved.nickname.isNullOrEmpty()) {
                    UserInfo(saved.userId, saved.nickname, saved.avatarUrl)
                } else null
            }
            initialized = true
        }
    }

    fun login(cookie: String, userId: Long? = null, nickname: String? = null, avatarUrl: String? = null) {
        _cookie.value = cookie
        _isLoggedIn.value = true
        _userInfo.value = if (nickname != null) UserInfo(userId, nickname, avatarUrl) else null
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveUserInfo(cookie, userId, nickname, avatarUrl)
        }
    }

    fun logout() {
        _cookie.value = null
        _isLoggedIn.value = false
        _userInfo.value = null
        CoroutineScope(Dispatchers.IO).launch {
            repository.clearCookie()
        }
    }
} 