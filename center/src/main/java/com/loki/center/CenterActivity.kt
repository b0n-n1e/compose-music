package com.loki.center

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.loki.center.home.HomeScreen
import com.loki.center.mine.MineScreen
import com.loki.center.navigation.center.BottomBarWidget
import com.loki.center.navigation.center.Screen
import com.loki.center.search.SearchScreen
import com.loki.center.ui.theme.ComposeMusicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
fun CenterScreen() {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Search, Screen.Mine)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBarWidget(
                navController = navController,
                items = items
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Search.route) { SearchScreen() }
            composable(Screen.Mine.route) { MineScreen() }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CenterScreenPreLight() {
    ComposeMusicTheme(ComposeMusicTheme.Theme.Light) {
        CenterScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun CenterScreenPreDark() {
    ComposeMusicTheme(ComposeMusicTheme.Theme.Dark) {
        CenterScreen()
    }
}