package com.loki.center.mine

import androidx.lifecycle.viewModelScope
import com.loki.center.architecture.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import com.loki.center.mine.*

@HiltViewModel
class MineViewModel @Inject constructor() : MviViewModel<MineState, MineIntent, MineEffect>() {

    private val _intent = MutableSharedFlow<MineIntent>()
    val intent = _intent.asSharedFlow()

    init {
        viewModelScope.launch {
            handleIntents()
        }
    }

    override fun createInitialState(): MineState = MineState()

    override suspend fun handleIntents() {
        intent.collect { intent ->
            when (intent) {
                is MineIntent.LoadUserInfo -> {
                    loadUserInfo()
                }
                is MineIntent.Login -> {
                    login()
                }
                is MineIntent.Logout -> {
                    logout()
                }
                is MineIntent.RefreshProfile -> {
                    refreshProfile()
                }
            }
        }
    }

    // 发送Intent的方法
    fun sendIntent(mineIntent: MineIntent) {
        viewModelScope.launch {
            _intent.emit(mineIntent)
        }
    }

    private fun loadUserInfo() {
        setState { copy(isLoading = true) }
        // TODO: 加载用户信息
        setState { copy(isLoading = false, isLoggedIn = true, userName = "测试用户") }
    }

    private fun login() {
        setEffect { MineEffect.NavigateToLogin }
    }

    private fun logout() {
        setState { copy(isLoggedIn = false, userName = "用户") }
        setEffect { MineEffect.ShowToast("已退出登录") }
    }

    private fun refreshProfile() {
        setState { copy(isLoading = true) }
        // TODO: 刷新用户资料
        setState { copy(isLoading = false) }
        setEffect { MineEffect.ShowToast("资料已更新") }
    }
} 