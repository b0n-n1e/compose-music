package com.loki.center.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.loki.center.ui.theme.ComposeMusicTheme

@Composable
fun MineScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ComposeMusicTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "MineScreen",
            color = ComposeMusicTheme.colors.textPrimary,
            fontSize = 24.sp
        )
    }
}