package com.loki.center.navigation.center

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.loki.center.ui.theme.ComposeMusicTheme



@Composable
fun BottomBarWidget(
    navController: NavController,
    items: List<Screen> = listOf(Screen.Home, Screen.Search, Screen.Mine)
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Row(
        modifier = Modifier
            .background(ComposeMusicTheme.colors.bottomBar)
            .navigationBarsPadding()
    ) {
        items.forEachIndexed { index, screen ->
            val isSelected = currentRoute == screen.route
            TabItem(
                iconVector = when (screen) {
                    is Screen.Home -> if (isSelected) Icons.Filled.Home else Icons.Outlined.Home
                    is Screen.Search -> if (isSelected) Icons.Filled.Search else Icons.Outlined.Search
                    is Screen.Mine -> if (isSelected) Icons.Filled.Person else Icons.Outlined.Person
                },
                label = when (screen) {
                    is Screen.Home -> "主页"
                    is Screen.Search -> "搜索"
                    is Screen.Mine -> "我的"
                },
                tint = if (isSelected) ComposeMusicTheme.colors.iconCurrent else ComposeMusicTheme.colors.icon,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
            )
        }
    }
}

@Composable
private fun TabItem(
    iconVector: ImageVector,
    label: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = 10.dp, bottom = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = iconVector,
            contentDescription = label,
            modifier= Modifier.size(24.dp),
            tint = tint
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = tint
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun BottomNavPreLight(){
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Search, Screen.Mine)
    ComposeMusicTheme(theme = ComposeMusicTheme.Theme.Light){
        BottomBarWidget(navController, items)
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavPreDark(){
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Search, Screen.Mine)
    ComposeMusicTheme(theme = ComposeMusicTheme.Theme.Dark){
        BottomBarWidget(navController, items)
    }
}