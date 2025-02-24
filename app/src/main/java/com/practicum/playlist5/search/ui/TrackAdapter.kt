package com.practicum.playlist5.search.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist5.R
import com.practicum.playlist5.search.domain.models.Track

class TrackAdapter() :
    RecyclerView.Adapter<TrackViewHolder>() {
    var onItemClickListener: TrackViewHolder.OnItemClickListener? = null
    var onItemLongClickListener: TrackViewHolder.OnItemLongClickListener? = null
    var tracks = mutableListOf<Track>()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tracklist, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position],onItemClickListener = onItemClickListener, onItemLongClickListener = onItemLongClickListener)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }


}