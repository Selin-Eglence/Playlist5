package com.practicum.playlist5.media

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlist5.audioplayer.ui.AudioPlayerActivity
import com.practicum.playlist5.databinding.FragmentFavoriteBinding
import com.practicum.playlist5.media.data.db.AppDatabase
import com.practicum.playlist5.search.domain.models.Track
import com.practicum.playlist5.search.ui.SearchFragment
import com.practicum.playlist5.search.ui.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import com.practicum.playlist5.search.ui.TrackAdapter
import com.practicum.playlist5.search.ui.TrackViewHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteFragment: Fragment() {
 private val favouriteViewModel:FavouriteViewModel by viewModel()
 private  var _binding: FragmentFavoriteBinding?= null
    private val binding get()= _binding!!

    private  var adapter: TrackAdapter?=null

    private var isClickAllowed = true



    private var audioPlayerLauncher: ActivityResultLauncher<Intent>? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        adapter = TrackAdapter { track -> openPlayer(track) }
        binding.favoriteList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.favoriteList.adapter = adapter

        audioPlayerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                favouriteViewModel.updateFavorites()  // Перезагрузка списка избранных
            }
        }

        favouriteViewModel.updateFavorites()

        favouriteViewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }




    private fun openPlayer(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            audioPlayerLauncher?.launch(intent)
        }
    }

    private fun render(state: FavouriteState) {
        when (state) {
            is FavouriteState.Content -> showContent(state.tracks)
            is FavouriteState.Empty -> showEmpty(state.message)
            is FavouriteState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.favoriteList.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        binding.placeholderText.visibility = View.GONE
    }

    private fun showContent(tracks: List<Track>) {
        binding.progressBar.visibility = View.GONE
        if (tracks.isNotEmpty()) {
            binding.favoriteList.visibility = View.VISIBLE
            adapter?.tracks?.addAll(tracks)
        }
    }

    private fun showEmpty(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderText.visibility = View.VISIBLE
        binding.placeholderText.text = message
        binding.favoriteList.visibility = View.GONE

        adapter?.tracks?.clear()
        adapter?.notifyDataSetChanged()

    }






    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter=null
    }


    companion object {
        const val TRACK_KEY = "track_key"
        fun newInstance() = FavouriteFragment()


    }
}