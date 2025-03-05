package com.practicum.playlist5.media.ui.playlist

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist5.R

class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val cover: ImageView = itemView.findViewById(R.id.playlist_cover)
    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
    private val trackCount: TextView = itemView.findViewById(R.id.playlist_tracks)



    fun bind(playlist: Playlist,onItemClickListener: OnItemClickListener?){
        if (playlist.imagePath?.isNotEmpty() == true) {
           cover.setImageURI(Uri.parse(playlist.imagePath))
        }
        playlistName.text = playlist.name
        val count = itemView.context.getFormattedCount(playlist.trackNum)
        Log.d("PlaylistViewHolder", "bind: playlist=${playlist.name}, trackNum=${playlist.trackNum}")
        trackCount.text = count

        itemView.setOnClickListener {
            onItemClickListener?.onItemClick(playlist)
        }


    }

    fun interface OnItemClickListener {
        fun onItemClick(item: Playlist)
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