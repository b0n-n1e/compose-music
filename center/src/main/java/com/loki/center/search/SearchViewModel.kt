package com.loki.center.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.utils.network.bean.search.Song
import com.loki.utils.network.service.SearchService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val searchResults: List<Song> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchService: SearchService
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    fun search(keyword: String) {
        if (keyword.isBlank()) {
            _uiState.value = SearchUiState()
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = searchService.searchSongs(keyword = keyword)
                _uiState.value = _uiState.value.copy(
                    searchResults = response.result?.songs ?: emptyList(),
                    isLoading = false
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}