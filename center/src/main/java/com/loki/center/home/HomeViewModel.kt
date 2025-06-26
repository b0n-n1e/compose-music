package com.loki.center.home

import androidx.lifecycle.viewModelScope
import com.loki.center.architecture.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import com.loki.center.home.*

@HiltViewModel
class HomeViewModel @Inject constructor() : MviViewModel<HomeState, HomeIntent, HomeEffect>() {

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
                is HomeIntent.LoadHomeData -> {
                    loadHomeData()
                }
                is HomeIntent.RefreshHome -> {
                    refreshHome()
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

    private fun loadHomeData() {
        setState { copy(isLoading = true) }
        // TODO: 加载首页数据
        setState { copy(isLoading = false) }
    }

    private fun refreshHome() {
        setState { copy(isLoading = true) }
        // TODO: 刷新首页数据
        setState { copy(isLoading = false) }
        setEffect { HomeEffect.ShowToast("首页已刷新") }
    }
} 