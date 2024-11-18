package com.practicum.playlist5.search.domain.models

sealed class SearchResult {
    data class Success(val tracks: List<Track>?, val expression: String) : SearchResult()
    data class Error(val message: String?) : SearchResult()
}
