package com.loki.center.home

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
import com.loki.center.home.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsState()

    // 处理Effect
    HandleEffects(
        effects = viewModel.effect,
        onEffect = { effect ->
            when (effect) {
                HomeEffect.NavigateToDetail -> {
                    // TODO: 导航到详情页
                    println("Navigate to detail")
                }
                is HomeEffect.ShowToast -> {
                }
            }
        }
    )

    // 加载初始数据
    LaunchedEffect(Unit) {
        viewModel.sendIntent(HomeIntent.LoadHomeData)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ComposeMusicTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = state.message,
            color = ComposeMusicTheme.colors.textPrimary,
            fontSize = 24.sp
        )
    }
}