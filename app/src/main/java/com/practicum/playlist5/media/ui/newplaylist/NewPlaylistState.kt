package com.practicum.playlist5.media.ui.newplaylist

sealed interface NewPlaylistState {
    data object Empty : NewPlaylistState

    data object NotEmpty : NewPlaylistState
}