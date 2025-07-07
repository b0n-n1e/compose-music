package com.loki.center.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingToolbar(
    modifier: Modifier = Modifier,
    headerHeight: Dp = 250.dp,
    toolbarHeight: Dp = 64.dp,
    navigationIcon: @Composable () -> Unit = {},
    header: @Composable () -> Unit,
    toolbar: @Composable () -> Unit,
    body: LazyListScope.() -> Unit
) {
    val lazyListState = rememberLazyListState()
    val density = LocalDensity.current

    val headerHeightPx = remember { with(density) { headerHeight.toPx() } }
    val toolbarHeightPx = remember { with(density) { toolbarHeight.toPx() } }

    val scrollCollapseThresholdPx = headerHeightPx - toolbarHeightPx

    val offset by remember {
        derivedStateOf {
            if (lazyListState.firstVisibleItemIndex == 0) {
                lazyListState.firstVisibleItemScrollOffset.toFloat()
            } else {
                scrollCollapseThresholdPx
            }
        }
    }

    val collapseProgress by remember {
        derivedStateOf { (offset / scrollCollapseThresholdPx).coerceIn(0f, 1f) }
    }

    val toolbarContentAlpha by remember {
        derivedStateOf { ((collapseProgress - 0.5f) * 2f).coerceIn(0f, 1f) }
    }

    Box(modifier = modifier.fillMaxSize()) {

        LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(headerHeight)
                ) {
                    header()
                }
            }
            body()
        }

        TopAppBar(
            title = {
                Row(modifier = Modifier.alpha(toolbarContentAlpha)) {
                    toolbar()
                }
            }, navigationIcon = navigationIcon, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = collapseProgress),
                scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = collapseProgress),
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
} 