package com.practicum.playlist5.ui.audio

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist5.creator.Creator
import com.practicum.playlist5.R
import com.practicum.playlist5.domain.api.AudioPlayerInteractor
import com.practicum.playlist5.ui.search.SearchActivity
import com.practicum.playlist5.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("DEPRECATION")
class AudioPlayerActivity : AppCompatActivity() {

    companion object{
        private const val TIMER_UPDATE_DELAY = 250L}


    private lateinit var track: Track
    private lateinit var play:ImageView
    private lateinit var progressTextView: TextView
    private lateinit var audioPlayerInteractor: AudioPlayerInteractor

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val handler = Handler(Looper.getMainLooper())

    private val timer by lazy { object: Runnable{
        override fun run() {
            if (audioPlayerInteractor.isPlaying()){
                progressTextView?.text = dateFormat.format(audioPlayerInteractor.getCurrentPosition())
                handler.postDelayed(this, TIMER_UPDATE_DELAY)
            }
        }
    } }



    @SuppressLint("ResourceType", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audioplayer_activity)

        audioPlayerInteractor = Creator.provideAudioPlayerInteractor()

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
        track = intent.getSerializableExtra(SearchActivity.TRACK_KEY) as Track
        play = findViewById(R.id.play)
        progressTextView= findViewById(R.id.playtracker)




        Glide.with(this)
            .load(track?.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(artworkUrl)




        trackName.text = track.trackName
        artistName.text=track.artistName
        album.text=track.collectionName
        year.text=track.releaseDate.substring(0, 4)
        genre.text=track.primaryGenreName
        country.text=track.country
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)



        audioPlayerInteractor.preparePlayer(track)


        play.setOnClickListener {
            playbackControl()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish() }


    private fun playbackControl() {
        if (audioPlayerInteractor.isPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }


    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        play.setImageResource(R.drawable.pause_icon)
        handler.post(timer)
    }

    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        play.setImageResource(R.drawable.play_icon)
        handler.removeCallbacks(timer)
    }



    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timer)
    }


}
