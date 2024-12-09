package com.practicum.playlist5.search.ui

import com.practicum.playlist5.search.domain.models.Track

sealed interface SearchState {
    data object Loading:SearchState
    data class SearchList(val tracks: MutableList<Track>) : SearchState
    data class HistoryList(val tracks: MutableList<Track>) : SearchState
    data class Error(val errorMessage: String) : SearchState
    data class NothingFound(val emptyMessage: String) : SearchState

    data object NoHistory:SearchState
}