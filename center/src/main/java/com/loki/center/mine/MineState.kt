package com.loki.center.mine

import com.loki.center.architecture.MviState
import com.loki.center.architecture.MviIntent
import com.loki.center.architecture.MviEffect
import com.loki.center.architecture.ToastEffect

// State: UI状态
data class MineState(
    val isLoading: Boolean = false,
    val message: String = "MineScreen",
    val userName: String = "用户",
    val isLoggedIn: Boolean = false
) : MviState

// Intent: 用户意图
sealed class MineIntent : MviIntent {
    data object LoadUserInfo : MineIntent()
    data object Login : MineIntent()
    data object Logout : MineIntent()
    data object RefreshProfile : MineIntent()
}

// Effect: 一次性效果
sealed class MineEffect : MviEffect {
    data class ShowToast(override val message: String) : MineEffect(), ToastEffect
    data object NavigateToLogin : MineEffect()
    data object NavigateToSettings : MineEffect()
} 