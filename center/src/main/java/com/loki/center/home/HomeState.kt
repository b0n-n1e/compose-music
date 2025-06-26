package com.loki.center.home

import com.loki.center.architecture.MviState
import com.loki.center.architecture.MviIntent
import com.loki.center.architecture.MviEffect
import com.loki.center.architecture.ToastEffect

// State: UI状态
data class HomeState(
    val isLoading: Boolean = false,
    val message: String = "HomeScreen"
) : MviState

// Intent: 用户意图
sealed class HomeIntent : MviIntent {
    data object LoadHomeData : HomeIntent()
    data object RefreshHome : HomeIntent()
}

// Effect: 一次性效果
sealed class HomeEffect : MviEffect {
    data class ShowToast(override val message: String) : HomeEffect(), ToastEffect
    data object NavigateToDetail : HomeEffect()
} 