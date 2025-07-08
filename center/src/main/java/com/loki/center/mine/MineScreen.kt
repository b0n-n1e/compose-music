package com.loki.center.mine

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.loki.center.ui.widget.CollapsingToolbar
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MineScreen(
    viewModel: MineViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsState()

    if (!state.isLoggedIn) {
        QrScreen(
            onLoginSuccess = { cookie ->
                viewModel.sendIntent(MineIntent.LoginWithCookie(cookie))
            }
        )
    } else {
        CollapsingToolbar(
            header = {
                HeaderContent(
                    username = state.userName,
                    userAvatarUrl = state.avatarUrl ?: "",
                    backgroundUrl = ""
                )
            },
            toolbar = {
                ToolbarContent(
                    username = state.userName,
                    userAvatarUrl = state.avatarUrl ?: ""
                )
            },
            body = {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "已登录", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "昵称: ${state.userName}", color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "UID: ${state.userId ?: "-"}", color = Color.Gray)
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(onClick = {
                            viewModel.sendIntent(MineIntent.Logout)
                        }) {
                            Text("退出登录")
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun HeaderContent(
    modifier: Modifier = Modifier,
    username: String,
    userAvatarUrl: String,
    backgroundUrl: String
) {
    Box(modifier = modifier.fillMaxSize()) {
        // 背景图片
        Image(
            painter = ColorPainter(MaterialTheme.colorScheme.primaryContainer),
            contentDescription = "背景",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 用户信息
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = ColorPainter(Color.Gray),
                contentDescription = "头像",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.height(16.dp))
            Text(username, style = MaterialTheme.typography.headlineSmall, color = Color.White)
        }
    }
}

@Composable
private fun ToolbarContent(
    modifier: Modifier = Modifier,
    username: String,
    userAvatarUrl: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = ColorPainter(Color.Gray),
            contentDescription = "头像",
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Text(username, style = MaterialTheme.typography.titleMedium)
    }
}


@Preview
@Composable
fun MineScreenPre(){
    MineScreen()
}