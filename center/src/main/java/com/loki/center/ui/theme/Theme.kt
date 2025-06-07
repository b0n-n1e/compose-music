package com.loki.center.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColorPalette = ComposeMusicColors(
    bottomBar = white1,
    background = white2,
    listItem = white,
    divider = white3,
    chatPage = white2,
    textPrimary = black3,
    textPrimaryMe = black3,
    textSecondary = grey1,
    onBackground = grey3,
    icon = black,
    iconCurrent = blue1,
    badge = red1,
    onBadge = white,
    bubbleMe = green1,
    bubbleOthers = white,
    textFieldBackground = white,
    more = grey4,
    chatPageBgAlpha = 0f,
)

private val DarkColorPalette = ComposeMusicColors(
    bottomBar = black1,
    background = black2,
    listItem = black3,
    divider = black4,
    chatPage = black2,
    textPrimary = white4,
    textPrimaryMe = black6,
    textSecondary = grey1,
    onBackground = grey1,
    icon = white5,
    iconCurrent = blue1,
    badge = red1,
    onBadge = white,
    bubbleMe = green2,
    bubbleOthers = black5,
    textFieldBackground = black7,
    more = grey5,
    chatPageBgAlpha = 0f,
)

@Immutable
class ComposeMusicColors(
    val bottomBar: Color,
    val background: Color,
    val listItem: Color,
    val divider: Color,
    val chatPage: Color,
    val textPrimary: Color,
    val textPrimaryMe: Color,
    val textSecondary: Color,
    val onBackground: Color,
    val icon: Color,
    val iconCurrent: Color,
    val badge: Color,
    val onBadge: Color,
    val bubbleMe: Color,
    val bubbleOthers: Color,
    val textFieldBackground: Color,
    val more: Color,
    val chatPageBgAlpha: Float,
)

private val LocalComposeMusicColors = staticCompositionLocalOf<ComposeMusicColors> {
    LightColorPalette
}

object ComposeMusicTheme {
    val colors: ComposeMusicColors
        @Composable
        get() = LocalComposeMusicColors.current

    enum class Theme {
        Light, Dark
    }
}

@Composable
fun ProvideComposeMusicColors(
    colors: ComposeMusicColors,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalComposeMusicColors provides colors, content = content)
}

@Composable
fun ComposeMusicTheme(
    theme: ComposeMusicTheme.Theme = if (isSystemInDarkTheme()) ComposeMusicTheme.Theme.Dark else ComposeMusicTheme.Theme.Light,
    content: @Composable () -> Unit,
) {
    val targetColors = when (theme) {
        ComposeMusicTheme.Theme.Light -> LightColorPalette
        ComposeMusicTheme.Theme.Dark -> DarkColorPalette
    }

    val colors = ComposeMusicColors(
        bottomBar = animateColorAsState(targetColors.bottomBar, tween(600)).value,
        background = animateColorAsState(targetColors.background, tween(600)).value,
        listItem = animateColorAsState(targetColors.listItem, tween(600)).value,
        divider = animateColorAsState(targetColors.divider, tween(600)).value,
        chatPage = animateColorAsState(targetColors.chatPage, tween(600)).value,
        textPrimary = animateColorAsState(targetColors.textPrimary, tween(600)).value,
        textPrimaryMe = animateColorAsState(targetColors.textPrimaryMe, tween(600)).value,
        textSecondary = animateColorAsState(targetColors.textSecondary, tween(600)).value,
        onBackground = animateColorAsState(targetColors.onBackground, tween(600)).value,
        icon = animateColorAsState(targetColors.icon, tween(600)).value,
        iconCurrent = animateColorAsState(targetColors.iconCurrent, tween(600)).value,
        badge = animateColorAsState(targetColors.badge, tween(600)).value,
        onBadge = animateColorAsState(targetColors.onBadge, tween(600)).value,
        bubbleMe = animateColorAsState(targetColors.bubbleMe, tween(600)).value,
        bubbleOthers = animateColorAsState(targetColors.bubbleOthers, tween(600)).value,
        textFieldBackground = animateColorAsState(targetColors.textFieldBackground, tween(600)).value,
        more = animateColorAsState(targetColors.more, tween(600)).value,
        chatPageBgAlpha = animateFloatAsState(targetColors.chatPageBgAlpha, tween(600)).value,
    )

    ProvideComposeMusicColors(colors) {
        MaterialTheme(
            typography = Typography(),
            content = content
        )
    }
}