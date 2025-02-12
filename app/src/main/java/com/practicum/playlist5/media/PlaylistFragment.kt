package com.practicum.playlist5.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlist5.R
import com.practicum.playlist5.databinding.FragmentPlaylistBinding
import com.practicum.playlist5.media.ui.model.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {

    private val playlistViewModel: PlaylistViewModel by viewModel()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get()= _binding!!
    private lateinit var playlistAdapter:PlaylistAdapter

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

        playlistViewModel.fillData()

        playlistViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.RefreshButton.setOnClickListener {
            findNavController().navigate(R.id.new_playlist_fragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
     companion object{
         fun newInstance() = PlaylistFragment()
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
        binding.placeholderText.text = message
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.playlistView.isVisible = true
        binding.placeholderImage.isVisible = false

        binding.playlistView.layoutManager = GridLayoutManager(requireContext(), 2)
        playlistAdapter = PlaylistAdapter()
        binding.playlistView.adapter = playlistAdapter
        playlistAdapter?.clear()
        playlistAdapter?.playlists?.addAll(playlists)
        playlistAdapter?.notifyDataSetChanged()
    }



}