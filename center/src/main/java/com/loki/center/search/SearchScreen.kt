package com.loki.center.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loki.center.CenterViewModel
import com.loki.center.ui.theme.ComposeMusicTheme
import com.loki.utils.extension.limitLength
import com.loki.utils.network.bean.search.Song
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun SearchScreen(
    viewModel: CenterViewModel = hiltViewModel()
) {
    var searchText by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // 防抖搜索
    LaunchedEffect(searchText) {
        snapshotFlow { searchText }.debounce(1000).collectLatest { keyword ->
                viewModel.search(keyword)
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ComposeMusicTheme.colors.background)
            .clickable(
                indication = null, interactionSource = remember { MutableInteractionSource() }) {
                keyboardController?.hide()
                focusManager.clearFocus()
            }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("搜索歌曲") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.search(searchText)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }))

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn {
                    items(uiState.searchResults, key = { it.id ?: it.hashCode() }) { song ->
                        SongItem(song = song)
                    }
                }
            }
        }
    }
}

@Composable
fun SongItem(song: Song) {
    val songName = (song.name ?: "Unknown Song").limitLength(20)
    val artistNames =
        (
            (song.artists?.joinToString{ it.name ?: "" }) ?: "Unknown Artist"
        ).limitLength(20)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = songName,
                color = ComposeMusicTheme.colors.textPrimary,
                fontSize = 16.sp
            )
            Text(
                text = artistNames,
                color = ComposeMusicTheme.colors.textSecondary,
                fontSize = 14.sp
            )
        }
    }
}