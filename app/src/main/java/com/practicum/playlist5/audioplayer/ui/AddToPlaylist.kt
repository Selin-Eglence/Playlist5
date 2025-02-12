package com.practicum.playlist5.audioplayer.ui

import com.practicum.playlist5.media.ui.model.Playlist

data class AddToPlaylist(val isAdded: Boolean,
                         val playlist: Playlist,
)
