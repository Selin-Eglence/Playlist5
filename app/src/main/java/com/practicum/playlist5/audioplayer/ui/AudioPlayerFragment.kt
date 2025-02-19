package com.practicum.playlist5.audioplayer.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlist5.R
import com.practicum.playlist5.audioplayer.domain.models.PlayerState
import com.practicum.playlist5.databinding.FragmentAudioplayerBinding
import com.practicum.playlist5.search.ui.SearchFragment
import com.practicum.playlist5.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment : Fragment() {

    private var _binding: FragmentAudioplayerBinding?=null
    private val binding get() = _binding!!
    private lateinit var track: Track
    private var playlistAdapter: SheetViewAdapter? = null
    private val viewModel by viewModel<AudioPlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            playlistAdapter?.submitList(playlists.orEmpty())
        }
        viewModel.loadPlaylists()

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }


        playlistAdapter = SheetViewAdapter { playlist ->
            viewModel.addTrackToPlaylist(playlist, track)
            Log.d("playlist", "загружены")
        }


        binding.bottomSheetRecyclerView.adapter = playlistAdapter
        binding.bottomSheetRecyclerView.isVisible = true
        viewModel.loadPlaylists()
        viewModel.addedToPlaylistState.observe(viewLifecycleOwner) { result ->
            when (result.isAdded) {
                true -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    Toast.makeText(
                        requireContext(),
                        "Трек успешно добавлен в плейлист ${result.playlist.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                false -> {
                    Toast.makeText(
                        requireContext(),
                        "Трек уже добавлен в плейлист ${result.playlist.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        track = arguments?.getSerializable(SearchFragment.TRACK_KEY) as Track

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

        binding.ivLike.setOnClickListener {
            val newIsFavorite = !track.isFavorite
            track = track.copy(isFavorite = newIsFavorite)
            updateLikeButton(newIsFavorite)
            viewModel.onFavouriteClicked(track)
        }

        viewModel.isFavourite.observe(viewLifecycleOwner, Observer {
            track = track.copy(isFavorite = it)
            updateLikeButton(it)
        })


        viewModel.playbackState.observe(viewLifecycleOwner) { state ->
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
            viewModel.onDestroy(track)
            PlayerState.STATE_COMPLETED
            requireActivity().onBackPressedDispatcher.onBackPressed()
            Log.e("back", "music")
        }

        binding.add.setOnClickListener {
            Log.d("add", "success")
            viewModel.loadPlaylists()
            binding.bottomSheetRecyclerView.isVisible = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.visibility = View.GONE
                    else -> binding.overlay.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                _binding?.overlay?.alpha = (slideOffset + 1f) / 2f
            }
        })

        binding.RefreshButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
        }
    }

    private fun updateLikeButton(isFavorite: Boolean) {
        val image = if (isFavorite) R.drawable.not_like else R.drawable.like
        binding.ivLike.setImageResource(image)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
