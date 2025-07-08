package com.loki.center.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loki.center.ui.theme.ComposeMusicTheme
import com.loki.center.common.mvi.HandleEffects
import com.loki.utils.extension.limitLength
import com.loki.utils.network.bean.search.Song
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.collectLatest

const val SEARCH_DELAY: Long = 1000

@OptIn(FlowPreview::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // 处理Effect
    HandleEffects(
        effects = viewModel.effect,
        onEffect = { effect ->
            when (effect) {
                SearchEffect.HideKeyboard -> {
                    keyboardController?.hide()
                }

                SearchEffect.ClearFocus -> {
                    focusManager.clearFocus()
                }

                is SearchEffect.ShowError -> {

                }

                is SearchEffect.ShowToast -> {

                }
            }
        }
    )

    // 防抖搜索
    LaunchedEffect(state.searchKeyword) {
        snapshotFlow {
            state.searchKeyword
        }.debounce(SEARCH_DELAY)
            .collectLatest { keyword ->
                if (keyword.isNotBlank()) {
                    viewModel.sendIntent(SearchIntent.SearchSongs(keyword))
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ComposeMusicTheme.colors.background)
            .clickable(
                indication = null, interactionSource = remember { MutableInteractionSource() }) {
                viewModel.sendIntent(SearchIntent.DismissKeyboard)
            }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.searchKeyword,
                onValueChange = { keyword ->
                    viewModel.sendIntent(SearchIntent.UpdateSearchKeyword(keyword))
                },
                label = { Text("搜索歌曲") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.sendIntent(SearchIntent.SearchSongs(state.searchKeyword))
                        viewModel.sendIntent(SearchIntent.DismissKeyboard)
                    })
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 错误状态显示
            state.errorMessage?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = androidx.compose.ui.graphics.Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn {
                    itemsIndexed(
                        items = state.searchResults,
                        key = { _, item -> item.id ?: item.hashCode() }
                    ) { index, song ->
                        SongItem(song = song)
                        if (index < state.searchResults.lastIndex) {
                            Divider(
                                color = ComposeMusicTheme.colors.divider,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp
                                ) // Indent divider to align with text
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SongItem(song: Song) {
    val songName = (song.name ?: "Unknown Song").limitLength(25)
    val artistNames =
        (song.artists?.joinToString { it.name ?: "" } ?: "Unknown Artist").limitLength(30)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Play song or navigate to detail */ }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = songName,
                color = ComposeMusicTheme.colors.textPrimary,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = artistNames,
                color = ComposeMusicTheme.colors.textSecondary,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}