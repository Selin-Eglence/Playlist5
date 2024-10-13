package com.practicum.playlist5.audioplayer.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist5.R
import com.practicum.playlist5.audioplayer.domain.models.PlayerState
import com.practicum.playlist5.databinding.AudioplayerActivityBinding
import com.practicum.playlist5.search.ui.SearchActivity
import com.practicum.playlist5.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("DEPRECATION")
class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: AudioplayerActivityBinding
    private lateinit var track: Track

    @SuppressLint("ResourceType", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AudioplayerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewModel by viewModel<AudioPlayerViewModel>()


        track = intent.getSerializableExtra(SearchActivity.TRACK_KEY) as Track


        Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.albumImage)

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.albumName.text = track.collectionName
        binding.yearName.text = track.releaseDate.substring(0, 4)
        binding.genreName.text = track.primaryGenreName
        binding.countryName.text = track.country
        binding.timing.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)


        viewModel.setTrack(track)


        viewModel.playbackState.observe(this) { state ->

            when (state.playerState) {
                PlayerState.STATE_PLAYING -> binding.play.setImageResource(R.drawable.pause_icon)
                else -> binding.play.setImageResource(R.drawable.play_icon)
            }


            binding.playtracker.text = state.progressText
        }


        binding.play.setOnClickListener {
            viewModel.playbackControl()
        }


        binding.lightMode.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
