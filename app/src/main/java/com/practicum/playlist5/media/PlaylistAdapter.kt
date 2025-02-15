package com.practicum.playlist5.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist5.R
import com.practicum.playlist5.media.ui.playlist.Playlist
import com.practicum.playlist5.media.ui.playlist.PlaylistViewHolder

class PlaylistAdapter() : RecyclerView.Adapter<PlaylistViewHolder>() {

    var playlists: MutableList<Playlist> = mutableListOf()

    fun clear() {
        playlists.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
}