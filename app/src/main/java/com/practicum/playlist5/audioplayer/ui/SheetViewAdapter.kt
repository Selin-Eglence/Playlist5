package com.practicum.playlist5.audioplayer.ui

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist5.R
import com.practicum.playlist5.media.ui.playlist.Playlist
import java.io.File

class SheetViewAdapter(
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<Playlist, SheetViewAdapter.SheetViewHolder>(PlaylistDiffCallback()) {

    class SheetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cover: ImageView = itemView.findViewById(R.id.playlist_cover)
        private val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
        private val trackCount: TextView = itemView.findViewById(R.id.playlist_tracks)

        fun bind(playlist: Playlist) {
            playlist.imagePath?.takeIf { it.isNotEmpty() }?.let { path ->
                cover.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(itemView)
                    .load(path)
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()
                    .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.radius_8dp)))
                    .into(cover)
            } ?: cover.setImageResource(R.drawable.placeholder)

            playlistName.text = playlist.name
            trackCount.text = itemView.context.getFormattedCount(playlist.trackNum)
        }

        private fun Context.getFormattedCount(trackNum: Int): String {
            val n = trackNum % 100
            return when {
                n in 11..14 -> getString(R.string.odd_track, trackNum)
                n % 10 == 1 -> getString(R.string.solo_track, trackNum)
                n % 10 in 2..4 -> getString(R.string.even_track, trackNum)
                else -> getString(R.string.odd_track, trackNum)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist_bottom_sheet, parent, false)
        return SheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: SheetViewHolder, position: Int) {
        val playlist = getItem(position)
        holder.bind(playlist)
        holder.itemView.setOnClickListener { onItemClickListener.onItemClick(playlist) }
    }

    class PlaylistDiffCallback : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean =
            oldItem == newItem
    }

    fun interface OnItemClickListener {
        fun onItemClick(item: Playlist)
    }
}