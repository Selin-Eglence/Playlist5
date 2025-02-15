package com.practicum.playlist5.audioplayer.ui

import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist5.R
import com.practicum.playlist5.media.ui.playlist.Playlist
import java.io.File

class SheetViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val cover: ImageView = itemView.findViewById(R.id.playlist_cover)
    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
    private val trackCount: TextView = itemView.findViewById(R.id.playlist_tracks)

    fun bind(playlist: Playlist){
        playlist.imagePath?.let { path ->
            if (path.isNotEmpty()) {
                cover.scaleType = ImageView.ScaleType.CENTER_CROP
                val filePath = File(
                    itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "cache"
                )
                val file = File(filePath, path)

                Glide.with(itemView)
                    .load(playlist.imagePath)
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()
                    .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.radius_8dp)))
                    .into(cover)
            }
        } ?: run {
            cover.setImageResource(R.drawable.placeholder)
        }
        playlistName.text = playlist.name
        val count = itemView.context.getFormattedCount(playlist.trackNum)
        trackCount.text = count

    }

    fun Context.getFormattedCount(trackNum: Int): String {
        val n = trackNum % 100

        return when {
            n in 11..14 -> String.format(getString(R.string.odd_track), trackNum)
            n % 10 == 1 -> String.format(getString(R.string.solo_track), trackNum)
            n % 10 in 2..4 -> String.format(getString(R.string.even_track), trackNum)
            else -> String.format(getString(R.string.odd_track), trackNum)
        }
    }
}