package com.loki.center.home

import com.loki.center.architecture.MviState
import com.loki.center.architecture.MviIntent
import com.loki.center.architecture.MviEffect
import com.loki.center.architecture.ToastEffect
import com.loki.utils.network.bean.home.Banner

// State: UI状态
data class HomeState(
    val isLoading: Boolean = false,
    val banners: List<Banner> = emptyList(),
    val errorMessage: String? = null
) : MviState

// Intent: 用户意图
sealed class HomeIntent : MviIntent {
    data object FetchBanners : HomeIntent()
    data object RefreshHome : HomeIntent()
    data class BannerClicked(val url: String) : HomeIntent()
}

// Effect: 一次性效果
sealed class HomeEffect : MviEffect {
    data class ShowToast(override val message: String) : HomeEffect(), ToastEffect
    data class NavigateToWeb(val url: String) : HomeEffect()
} 