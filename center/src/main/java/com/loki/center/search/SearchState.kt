package com.loki.center.search

import com.loki.center.architecture.MviState
import com.loki.center.architecture.MviIntent
import com.loki.center.architecture.MviEffect
import com.loki.center.architecture.ToastEffect
import com.loki.utils.network.bean.search.Song

// State: UI状态
data class SearchState(
    val searchResults: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val searchKeyword: String = "",
    val errorMessage: String? = null
) : MviState

// Intent: 用户意图
sealed class SearchIntent : MviIntent {
    data class SearchSongs(val keyword: String) : SearchIntent()
    data class UpdateSearchKeyword(val keyword: String) : SearchIntent()
    data object ClearSearch : SearchIntent()
    data object RetrySearch : SearchIntent()
    data object DismissKeyboard : SearchIntent()
}

// Effect: 一次性效果
sealed class SearchEffect : MviEffect {
    data class ShowToast(override val message: String) : SearchEffect(), ToastEffect
    data class ShowError(override val message: String) : SearchEffect(), ToastEffect
    data object HideKeyboard : SearchEffect()
    data object ClearFocus : SearchEffect()
} 