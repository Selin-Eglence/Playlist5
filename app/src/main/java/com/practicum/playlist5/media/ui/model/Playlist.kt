package com.practicum.playlist5.media.ui.model

data class Playlist(
    val id: Int=0,
                     val name: String,
                     val description: String,
                     val imagePath: String?,
                     val tracks: List<String>,
    val trackNum: Int = 0,
    )
