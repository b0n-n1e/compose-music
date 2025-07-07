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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loki.center.ui.widget.CollapsingToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MineScreen(
    viewModel: MineViewModel = hiltViewModel(),
) {
    // 假设这些数据从ViewModel的State中获取
    val username = "用户名"
    val userAvatarUrl = "" // 使用占位符
    val backgroundUrl = "" // 使用占位符

    CollapsingToolbar(
        navigationIcon = {
            IconButton(onClick = { /* TODO: 返回操作 */ }) {
                Icon(Icons.Filled.Menu, contentDescription = "返回")
            }
        },
        header = {
            HeaderContent(
                username = username,
                userAvatarUrl = userAvatarUrl,
                backgroundUrl = backgroundUrl
            )
        },
        toolbar = {
            ToolbarContent(
                username = username,
                userAvatarUrl = userAvatarUrl
            )
        }
    ) {
        items(50) { index ->
            Text(
                text = "列表项 #${index + 1}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
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
