package com.practicum.playlist5.search.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist5.R
import com.practicum.playlist5.search.domain.models.Track

class TrackAdapter(private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = mutableListOf<Track>()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tracklist, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun interface ItemClickListener {
        fun onItemClick(track: Track)
    }


}