package com.practicum.playlist5.media.ui.playlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Playlist(
    val id: Long=0,
                     val name: String,
                     val description: String,
                     val imagePath: String?,
                     val tracks: List<Long>,
    val trackNum: Int = 0,
    ):Parcelable
