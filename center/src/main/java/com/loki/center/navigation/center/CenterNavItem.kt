package com.loki.center.navigation.center

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class CenterNavItem(
    val label : String,
    val selectItemRes : ImageVector,
    val unSelectItemRes : ImageVector
){
    companion object{
        val mCenterNavItem = listOf(
            CenterNavItem("home", Icons.Default.Home, Icons.Default.Home),
            CenterNavItem("mine", Icons.Default.Person, Icons.Default.Person),
            CenterNavItem("search", Icons.Default.Search, Icons.Default.Search),
        )
    }
}

