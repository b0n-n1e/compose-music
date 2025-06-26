package com.loki.center.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loki.center.ui.theme.ComposeMusicTheme
import com.loki.center.architecture.HandleEffects

@Composable
fun MineScreen(
    viewModel: MineViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsState()

    // 处理Effect
    HandleEffects(
        effects = viewModel.effect,
        onEffect = { effect ->
            when (effect) {
                MineEffect.NavigateToLogin -> {
                    // TODO: 导航到登录页
                    println("Navigate to login")
                }
                MineEffect.NavigateToSettings -> {
                    // TODO: 导航到设置页
                    println("Navigate to settings")
                }
                is MineEffect.ShowToast -> {
                }
            }
        }
    )

    // 加载初始数据
    LaunchedEffect(Unit) {
        viewModel.sendIntent(MineIntent.LoadUserInfo)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ComposeMusicTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (state.isLoggedIn) "${state.message} - ${state.userName}" else state.message,
            color = ComposeMusicTheme.colors.textPrimary,
            fontSize = 24.sp
        )
    }
}