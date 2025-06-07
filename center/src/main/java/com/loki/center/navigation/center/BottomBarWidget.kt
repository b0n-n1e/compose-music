package com.loki.center.navigation.center

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loki.center.ui.theme.ComposeMusicTheme



@Composable
fun BottomBarWidget(
    selectedPosition : Int,
    bottomItems : List<CenterNavItem>,
    onItemSelected: (position : Int) -> Unit
) {
    BottomNavigation(
        backgroundColor = ComposeMusicTheme.colors.bottomBar
    ) {
        bottomItems.forEachIndexed {index, item ->
            BottomNavigationItem(
                selected = selectedPosition == index,
                onClick = {onItemSelected.invoke(index)},
                icon = {
                    var iconRes = item.unSelectItemRes
                    var iconColor  = ComposeMusicTheme.colors.icon
                    if (selectedPosition == index){
                        iconRes = item.selectItemRes
                        iconColor = ComposeMusicTheme.colors.iconCurrent
                    }
                    Icon(
                        imageVector = iconRes,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(),
                        tint = iconColor
                    )
                },
                label = {
                    val labelStyle = if(selectedPosition==index){
                        TextStyle(
                            fontWeight = FontWeight.Medium,
                            color = ComposeMusicTheme.colors.iconCurrent,
                            fontSize = 11.sp
                        )
                    }else{
                        TextStyle(
                            fontWeight = FontWeight.Normal,
                            color = ComposeMusicTheme.colors.icon,
                            fontSize = 11.sp
                        )
                    }

                    Text(
                        text = bottomItems[index].label,
                        style = labelStyle
                    )
                }

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomBarWidgetLight() {
    ComposeMusicTheme (ComposeMusicTheme.Theme.Light) {
        BottomBarWidget(0, CenterNavItem.mCenterNavItem, {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomBarWidgetNight() {
    ComposeMusicTheme (ComposeMusicTheme.Theme.Dark) {
        BottomBarWidget(0, CenterNavItem.mCenterNavItem, {})
    }
}