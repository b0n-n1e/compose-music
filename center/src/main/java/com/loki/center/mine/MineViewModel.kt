package com.loki.center.mine

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.loki.center.common.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import com.loki.utils.network.service.UserService
import com.loki.utils.datastroe.UserManager
import com.loki.utils.extension.TAG
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class MineViewModel @Inject constructor(
    private val userService: UserService
) : MviViewModel<MineState, MineIntent, MineEffect>() {

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
                is MineIntent.LoginWithCookie -> {
                    handleLogin(intent.cookie)
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
        setState { copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userService.logout() // 先请求后端，自动带cookie
            } catch (e: Exception) {
                // 可忽略或提示
            }
            UserManager.logout() // 再清空本地
            setState {
                copy(
                    isLoading = false,
                    isLoggedIn = false,
                    userName = "用户",
                    userId = null,
                    avatarUrl = null
                )
            }
            setEffect { MineEffect.ShowToast("已退出登录") }
        }
    }

    private fun refreshProfile() {
        setState { copy(isLoading = true) }
        // TODO: 刷新用户资料
        setState { copy(isLoading = false) }
        setEffect { MineEffect.ShowToast("资料已更新") }
    }

    private fun handleLogin(cookie: String) {
        setState { copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val accountResp = userService.getAccount()
                val uid = accountResp.profile?.userId
                var nickname: String? = null
                var avatarUrl: String? = null
                if (uid != null) {
                    val detailResp = userService.getUserDetail(uid)
                    nickname = detailResp.profile?.nickname
                    avatarUrl = detailResp.profile?.avatarUrl
                }
                Log.d(this@MineViewModel.TAG, "handleLogin: $nickname + $avatarUrl + $uid")
                UserManager.login(cookie, uid, nickname, avatarUrl)
                setState {
                    copy(
                        isLoading = false,
                        isLoggedIn = true,
                        userName = nickname ?: "-",
                        userId = uid,
                        avatarUrl = avatarUrl
                    )
                }
                setEffect { MineEffect.ShowToast("登录成功") }
            } catch (e: Exception) {
                setState { copy(isLoading = false) }
                setEffect { MineEffect.ShowToast("登录失败: ${e.message}") }
            }
        }
    }
} 