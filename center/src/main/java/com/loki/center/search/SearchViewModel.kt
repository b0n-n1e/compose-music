package com.loki.center.search

import androidx.lifecycle.viewModelScope
import com.loki.center.common.mvi.MviViewModel
import com.loki.utils.network.service.SearchService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchService: SearchService
) : MviViewModel<SearchState, SearchIntent, SearchEffect>() {

    private val _intent = MutableSharedFlow<SearchIntent>()
    val intent = _intent.asSharedFlow()

    init {
        viewModelScope.launch {
            handleIntents()
        }
    }

    override fun createInitialState(): SearchState = SearchState()

    override suspend fun handleIntents() {
        intent.collect { intent ->
            when (intent) {
                is SearchIntent.SearchSongs -> {
                    searchSongs(intent.keyword)
                }
                is SearchIntent.UpdateSearchKeyword -> {
                    updateSearchKeyword(intent.keyword)
                }
                is SearchIntent.ClearSearch -> {
                    clearSearch()
                }
                is SearchIntent.RetrySearch -> {
                    retrySearch()
                }
                is SearchIntent.DismissKeyboard -> {
                    setEffect { SearchEffect.HideKeyboard }
                    setEffect { SearchEffect.ClearFocus }
                }
            }
        }
    }

    // 发送Intent的方法
    fun sendIntent(searchIntent: SearchIntent) {
        viewModelScope.launch {
            _intent.emit(searchIntent)
        }
    }

    private fun searchSongs(keyword: String) {
        if (keyword.isBlank()) {
            setState {
                copy(
                    searchResults = emptyList(),
                    isLoading = false,
                    errorMessage = null
                )
            }
            return
        }

        setState {
            copy(
                isLoading = true,
                errorMessage = null
            )
        }
        
        viewModelScope.launch {
            try {
                val response = searchService.searchSongs(keyword = keyword)
                setState { 
                    copy(
                        searchResults = response.result?.songs ?: emptyList(),
                        isLoading = false,
                        searchKeyword = keyword
                    )
                }
                
                if (response.result?.songs.isNullOrEmpty()) {
                    setEffect { SearchEffect.ShowToast("未找到相关歌曲") }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                setState { 
                    copy(
                        isLoading = false, 
                        errorMessage = "搜索失败: ${e.message}"
                    )
                }
                setEffect { SearchEffect.ShowError("搜索失败: ${e.message}") }
            }
        }
    }

    private fun updateSearchKeyword(keyword: String) {
        setState { copy(searchKeyword = keyword) }
    }

    private fun clearSearch() {
        setState { 
            copy(
                searchResults = emptyList(),
                searchKeyword = "",
                errorMessage = null
            )
        }
        setEffect { SearchEffect.HideKeyboard }
        setEffect { SearchEffect.ClearFocus }
    }

    private fun retrySearch() {
        val currentKeyword = viewState.value.searchKeyword
        if (currentKeyword.isNotBlank()) {
            searchSongs(currentKeyword)
        }
    }
}