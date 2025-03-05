package com.practicum.playlist5.media.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlist5.R
import com.practicum.playlist5.databinding.FragmentPlaylistBinding
import com.practicum.playlist5.media.MediaFragmentDirections
import com.practicum.playlist5.media.PlaylistAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {

    private val playlistViewModel: PlaylistViewModel by viewModel()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get()= _binding!!
    private lateinit var playlistAdapter: PlaylistAdapter
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentPlaylistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isClickAllowed = true
        playlistViewModel.fillData()

        playlistViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.RefreshButton.setOnClickListener {
            val directions = MediaFragmentDirections.actionMediaToNewPlaylistFragment(null)
            findNavController().navigate(directions)
        }

        binding.playlistView.layoutManager = GridLayoutManager(requireContext(), 2)
        playlistAdapter = PlaylistAdapter().apply {
            onItemClickListener = PlaylistViewHolder.OnItemClickListener { playlist ->
                intentOnePlaylistFragment(playlist)
            }
        }
        binding.playlistView.adapter = playlistAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
     companion object{
         fun newInstance() = PlaylistFragment()
         const val PLAYLIST = "playlist_key"
         private const val DEBOUNCE_DELAY = 1000L
     }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> showContent(state.playlists)
            is PlaylistState.Empty -> showEmpty(state.message)
            PlaylistState.Loading -> PlaylistState.Loading
        }
    }

    private fun showEmpty(message: String) {
        binding.playlistView.isVisible = false
        binding.placeholderImage.isVisible = true
        binding.placeholderText.isVisible=true
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.playlistView.isVisible = true
        binding.placeholderImage.isVisible = false
        binding.placeholderText.isVisible=false
        playlistAdapter?.clear()
        playlistAdapter?.playlists?.addAll(playlists)
        playlistAdapter?.notifyDataSetChanged()
    }

    private fun intentOnePlaylistFragment(playlist: Playlist) {
        if (clickDebounce()) {
            val bundle = Bundle().apply {
                putLong(PLAYLIST, playlist.id)
            }
            findNavController().navigate(R.id.action_mediaFragment_to_onePlaylistFragment, bundle)
        }
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }



}