package com.practicum.playlist5

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("DEPRECATION")
class MediaActivity : AppCompatActivity() {


    private lateinit var track: Track

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val arrow = findViewById<Button>(R.id.light_mode)
        arrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val trackName = findViewById<TextView>(R.id.trackName)
        val artistName = findViewById<TextView>(R.id.artistName)
        val duration = findViewById<TextView>(R.id.timing)
        val album = findViewById<TextView>(R.id.album_name)
        val year = findViewById<TextView>(R.id.year_name)
        val genre = findViewById<TextView>(R.id.genre_name)
        val country = findViewById<TextView>(R.id.country_name)
        val artworkUrl = findViewById<ImageView>(R.id.albumImage)
        val track = intent.getSerializableExtra(SearchActivity.TRACK_KEY) as Track



            if (track.artworkUrl512.isNullOrEmpty()) {
                artworkUrl.setImageResource(R.drawable.placeholder)
            }
              else{  Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(artworkUrl)}




        trackName.text = track.trackName
        artistName.text=track.artistName
        album.text=track.collectionName
        year.text=track.releaseDate.substring(0, 4)
        genre.text=track.primaryGenreName
        country.text=track.country
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish() }
}
