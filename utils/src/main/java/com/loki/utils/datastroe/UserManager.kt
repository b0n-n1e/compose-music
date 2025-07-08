package com.loki.utils.datastroe

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object UserManager {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _cookie = MutableStateFlow<String?>(null)
    val cookie: StateFlow<String?> = _cookie.asStateFlow()

    private lateinit var repository: LoginRepository
    private var initialized = false

    fun init(context: Context) {
        if (!initialized) {
            repository = LoginRepository.getInstance(context)
            CoroutineScope(Dispatchers.IO).launch {
                val savedCookie = repository.getCookie()
                _cookie.value = savedCookie
                _isLoggedIn.value = !savedCookie.isNullOrEmpty()
            }
            initialized = true
        }
    }

    fun login(cookie: String) {
        _cookie.value = cookie
        _isLoggedIn.value = true
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveCookie(cookie)
        }
    }

    fun logout() {
        _cookie.value = null
        _isLoggedIn.value = false
        CoroutineScope(Dispatchers.IO).launch {
            repository.clearCookie()
        }
    }
} 