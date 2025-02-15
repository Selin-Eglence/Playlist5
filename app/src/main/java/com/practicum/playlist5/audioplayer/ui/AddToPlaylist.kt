package com.practicum.playlist5.audioplayer.ui

import com.practicum.playlist5.media.ui.playlist.Playlist

data class AddToPlaylist(val isAdded: Boolean,
                         val playlist: Playlist,
)
