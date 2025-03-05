package com.practicum.playlist5.audioplayer.ui

import android.widget.Button
import com.practicum.playlist5.audioplayer.domain.models.PlayerState

data class ScreenState(val isPlayButtonEnabled: Boolean,val progressText: String, val playerState: PlayerState)
