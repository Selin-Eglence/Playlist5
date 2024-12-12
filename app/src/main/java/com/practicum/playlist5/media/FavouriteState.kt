package com.practicum.playlist5.media

import com.practicum.playlist5.search.domain.models.Track

sealed interface FavouriteState {
    object Loading : FavouriteState

    data class Content(
        val tracks: List<Track>
    ): FavouriteState
    data class Empty(
        val message:String
    ): FavouriteState
}

