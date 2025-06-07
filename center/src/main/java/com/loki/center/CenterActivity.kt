package com.loki.center

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.loki.center.ui.theme.ComposeMusicTheme

class CenterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMusicTheme {
                CenterScreen()
            }
        }
    }
}

@Composable
fun CenterScreen(){

}