package com.loki.center.home

import androidx.lifecycle.viewModelScope
import com.loki.center.architecture.MviViewModel
import com.loki.utils.network.service.HomeService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import com.loki.center.home.*

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeService: HomeService
) : MviViewModel<HomeState, HomeIntent, HomeEffect>() {

    private val _intent = MutableSharedFlow<HomeIntent>()
    val intent = _intent.asSharedFlow()

    init {
        viewModelScope.launch {
            handleIntents()
        }
    }

    override fun createInitialState(): HomeState = HomeState()

    override suspend fun handleIntents() {
        intent.collect { intent ->
            when (intent) {
                is HomeIntent.FetchBanners -> {
                    fetchBanners()
                }
                is HomeIntent.RefreshHome -> {
                    refreshHome()
                }
                is HomeIntent.BannerClicked -> {
                    setEffect { HomeEffect.NavigateToWeb(intent.url) }
                }
            }
        }
    }

    // 发送Intent的方法
    fun sendIntent(homeIntent: HomeIntent) {
        viewModelScope.launch {
            _intent.emit(homeIntent)
        }
    }

    fun setEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun fetchBanners() {
        if (viewState.value.banners.isNotEmpty()) return // Avoid refetching
        setState { copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val response = homeService.getBanners()
                val validBanners = response.banners?.filterNotNull() ?: emptyList()
                setState { copy(isLoading = false, banners = validBanners) }
            } catch (e: Exception) {
                e.printStackTrace()
                setState { copy(isLoading = false, errorMessage = "无法加载轮播图: ${e.message}") }
            }
        }
    }

    private fun refreshHome() {
        setState { copy(isLoading = true, errorMessage = null, banners = emptyList()) }
        fetchBanners()
        setEffect { HomeEffect.ShowToast("首页已刷新") }
    }
} 