package com.loki.center.mine

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.loki.center.ui.widget.CollapsingToolbar
import com.loki.center.mine.QrLoginDialog
import com.loki.center.mine.QrLoginViewModel
import com.loki.center.mine.QrScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MineScreen() {
    // 假设用remember保存登录状态和cookie，后续可用ViewModel/Repository全局管理
    var isLoggedIn by remember { mutableStateOf(false) }
    var userCookie by remember { mutableStateOf("") }

    if (!isLoggedIn) {
        QrScreen(
            onLoginSuccess = { cookie ->
                userCookie = cookie
                isLoggedIn = true
                // TODO: 持久化cookie，拉取用户信息
            }
        )
    } else {
        // 已登录，展示用户信息
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 这里可以展示头像、昵称等
            Text(text = "已登录", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Cookie: $userCookie", color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                // 退出登录
                isLoggedIn = false
                userCookie = ""
                // TODO: 清除本地cookie和用户信息
            }) {
                Text("退出登录")
            }
        }
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