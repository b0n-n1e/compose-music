package com.loki.center.architecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.SharedFlow
import com.loki.utils.extension.showToast

/**
 * 通用Effect处理器
 */
@Composable
fun <T : MviEffect> HandleEffects(
    effects: SharedFlow<T>,
    onEffect: (T) -> Unit
) {
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        effects.collect { effect ->
            // 处理通用的Toast效果
            when (effect) {
                is ToastEffect -> {
                    context.showToast(effect.message)
                }
                else -> {
                    onEffect(effect)
                }
            }
        }
    }
} 