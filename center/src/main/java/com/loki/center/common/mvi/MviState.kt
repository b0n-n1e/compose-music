package com.loki.center.common.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * MVI架构基础接口
 * State: UI状态
 * Intent: 用户意图
 * Effect: 一次性效果（如导航、显示Toast等）
 */
interface MviState

interface MviIntent

interface MviEffect

/**
 * 通用Toast效果接口
 */
interface ToastEffect : MviEffect {
    val message: String
}

/**
 * MVI架构ViewModel基类
 */
abstract class MviViewModel<State : MviState, Intent : MviIntent, Effect : MviEffect> : ViewModel() {
    private val initialState: State by lazy { createInitialState() }
    abstract fun createInitialState(): State

    private val _viewState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val viewState: StateFlow<State> = _viewState.asStateFlow()

    val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
    val effect: SharedFlow<Effect> = _effect.asSharedFlow()

    abstract suspend fun handleIntents()

    protected fun setState(reduce: State.() -> State) {
        val newState = viewState.value.reduce()
        _viewState.value = newState
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.emit(effectValue) }
    }
} 