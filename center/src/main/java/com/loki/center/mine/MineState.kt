package com.loki.center.mine

import com.loki.center.common.mvi.MviState
import com.loki.center.common.mvi.MviIntent
import com.loki.center.common.mvi.MviEffect
import com.loki.center.common.mvi.ToastEffect

// State: UI状态
data class MineState(
    val isLoading: Boolean = false,
    val message: String = "MineScreen",
    val userName: String = "用户",
    val isLoggedIn: Boolean = false,
    val userId: Long? = null,
    val avatarUrl: String? = null
) : MviState

// Intent: 用户意图
sealed class MineIntent : MviIntent {
    data object LoadUserInfo : MineIntent()
    data object Login : MineIntent()
    data object Logout : MineIntent()
    data object RefreshProfile : MineIntent()
    data class LoginWithCookie(val cookie: String) : MineIntent()
}

// Effect: 一次性效果
sealed class MineEffect : MviEffect {
    data class ShowToast(override val message: String) : MineEffect(), ToastEffect
    data object NavigateToLogin : MineEffect()
    data object NavigateToSettings : MineEffect()
} 