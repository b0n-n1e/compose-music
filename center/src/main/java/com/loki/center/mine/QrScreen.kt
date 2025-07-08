package com.loki.center.mine

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import com.loki.center.ui.theme.ComposeMusicTheme

@Composable
fun Base64QrImage(base64: String, modifier: Modifier = Modifier) {
    var imageBitmap by remember(base64) { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    LaunchedEffect(base64) {
        try {
            val pureBase64 = base64.substringAfter(",")
            val bytes = Base64.decode(pureBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            imageBitmap = bitmap?.asImageBitmap()
        } catch (_: Exception) {
            imageBitmap = null
        }
    }
    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap!!,
            contentDescription = "二维码",
            modifier = modifier
        )
    }
}

@Composable
fun QrScreen(
    onLoginSuccess: (cookie: String) -> Unit,
    viewModel: QrLoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var qrImgCache by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState) {
        if (uiState is QrLoginUiState.ShowQr) {
            qrImgCache = (uiState as QrLoginUiState.ShowQr).qrImg
        }
    }

    LaunchedEffect(Unit) {
        viewModel.startQrLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ComposeMusicTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            Text(
                text = "扫码登录",
                style = MaterialTheme.typography.titleLarge,
                color = ComposeMusicTheme.colors.textPrimary
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (!qrImgCache.isNullOrEmpty()) {
                Base64QrImage(
                    base64 = qrImgCache ?: "",
                    modifier = Modifier.size(240.dp)
                )
            } else if (uiState is QrLoginUiState.Loading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(12.dp))
            } else {
                Text("二维码加载失败", color = Color.Red)
            }
            Spacer(modifier = Modifier.height(12.dp))
            when (uiState) {
                is QrLoginUiState.Loading -> {
                    Text("正在获取二维码...", color = ComposeMusicTheme.colors.textSecondary)
                }
                is QrLoginUiState.Status -> {
                    Text(
                        text = (uiState as QrLoginUiState.Status).statusMsg,
                        color = ComposeMusicTheme.colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
                is QrLoginUiState.Error -> {
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
                is QrLoginUiState.ShowQr -> {
                    Button(onClick = { viewModel.refreshQr() }) {
                        Text("刷新二维码")
                    }
                }
                is QrLoginUiState.Success -> {
                    Text(
                        text = "登录成功！",
                        color = Color(0xFF4CAF50),
                        style = MaterialTheme.typography.titleMedium
                    )
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