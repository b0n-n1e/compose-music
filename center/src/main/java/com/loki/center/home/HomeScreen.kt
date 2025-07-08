package com.loki.center.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.loki.center.common.mvi.HandleEffects
import com.loki.center.ui.theme.ComposeMusicTheme
import com.loki.utils.network.bean.home.Banner
import androidx.core.net.toUri

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsState()
    val context = LocalContext.current

    HandleEffects(
        effects = viewModel.effect,
        onEffect = { effect ->
            when (effect) {
                is HomeEffect.NavigateToWeb -> {
                    val intent = Intent(Intent.ACTION_VIEW, effect.url.toUri())
                    context.startActivity(intent)
                }
                is HomeEffect.ShowToast -> {
                    // Toast is handled by the generic handler
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.sendIntent(HomeIntent.FetchBanners)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ComposeMusicTheme.colors.background)
    ) {
        if (state.isLoading && state.banners.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                // You can add a CircularProgressIndicator here
            }
        } else if (state.errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.errorMessage!!, color = ComposeMusicTheme.colors.textPrimary)
            }
        } else if (state.banners.isNotEmpty()) {
            BannerCarousel(banners = state.banners) { banner ->
                banner.url?.let {
                    viewModel.sendIntent(HomeIntent.BannerClicked(it))
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerCarousel(
    banners: List<Banner>,
    onBannerClick: (Banner) -> Unit
) {
    val pagerState = rememberPagerState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        HorizontalPager(
            count = banners.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 24.dp),
            itemSpacing = 12.dp,
            modifier = Modifier.height(180.dp)
        ) { page ->
            val banner = banners[page]
            AsyncImage(
                model = banner.pic,
                contentDescription = banner.typeTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onBannerClick(banner) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = ComposeMusicTheme.colors.iconCurrent,
            inactiveColor = ComposeMusicTheme.colors.divider,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}