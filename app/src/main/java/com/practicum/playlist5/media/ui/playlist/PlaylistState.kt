package com.practicum.playlist5.media.ui.playlist

sealed interface PlaylistState{

    object Loading : PlaylistState
    data class Content(val playlists: List<Playlist>) : PlaylistState
    data class Empty(
        val message: String
    ) : PlaylistState
}