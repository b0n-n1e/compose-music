package com.loki.center.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.loki.center.ui.theme.ComposeMusicTheme

@Composable
fun QrLoginDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    viewModel: QrLoginViewModel,
    onLoginSuccess: (cookie: String) -> Unit
) {
    if (!show) return
    val uiState by viewModel.uiState.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = ComposeMusicTheme.colors.background,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "关闭")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "扫码登录",
                    style = MaterialTheme.typography.titleLarge,
                    color = ComposeMusicTheme.colors.textPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))
                when (uiState) {
                    is QrLoginUiState.Loading -> {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("正在获取二维码...", color = ComposeMusicTheme.colors.textSecondary)
                    }
                    is QrLoginUiState.ShowQr -> {
                        val qrImg = (uiState as QrLoginUiState.ShowQr).qrImg
                        if (!qrImg.isNullOrEmpty()) {
                            AsyncImage(
                                model = "data:image/png;base64,$qrImg",
                                contentDescription = "二维码",
                                modifier = Modifier.size(200.dp)
                            )
                        } else {
                            Text("二维码加载失败", color = Color.Red)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.refreshQr() }) {
                            Text("刷新二维码")
                        }
                    }
                    is QrLoginUiState.Status -> {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = (uiState as QrLoginUiState.Status).statusMsg,
                            color = ComposeMusicTheme.colors.textSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                    is QrLoginUiState.Error -> {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = (uiState as QrLoginUiState.Error).msg,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.refreshQr() }) {
                            Text("重新获取二维码")
                        }
                    }
                    is QrLoginUiState.Success -> {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "登录成功！",
                            color = Color(0xFF4CAF50),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }

    // 登录成功回调
    LaunchedEffect(uiState) {
        if (uiState is QrLoginUiState.Success) {
            onLoginSuccess((uiState as QrLoginUiState.Success).cookie)
        }
    }
} 