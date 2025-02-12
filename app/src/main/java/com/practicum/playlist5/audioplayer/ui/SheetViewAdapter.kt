package com.practicum.playlist5.audioplayer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist5.R
import com.practicum.playlist5.media.ui.model.Playlist

class SheetViewAdapter (private val playlists: List<Playlist>,
                        private val onItemClickListener: OnItemClickListener,
) : RecyclerView.Adapter<SheetViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SheetViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_playlist_bottom_sheet, parent, false)
        return SheetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: SheetViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { onItemClickListener.onItemClick(playlists[position]) }
    }

    class PlaylistDiffCallback : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean =
            oldItem == newItem
    }

}


fun interface OnItemClickListener {
    fun onItemClick(item: Playlist)
}