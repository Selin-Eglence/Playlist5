package compackage

import com.practicum.playlist5.media.FavouriteState
import com.practicum.playlist5.media.FavouriteViewModel
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlist5.R
import com.practicum.playlist5.databinding.FragmentFavoriteBinding
import com.practicum.playlist5.media.data.db.AppDatabase
import com.practicum.playlist5.search.domain.models.Track
import com.practicum.playlist5.search.ui.SearchFragment
import com.practicum.playlist5.search.ui.SearchFragment.Companion
import com.practicum.playlist5.search.ui.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import com.practicum.playlist5.search.ui.TrackAdapter
import com.practicum.playlist5.search.ui.TrackViewHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteFragment: Fragment() {
    private val favouriteViewModel: FavouriteViewModel by viewModel()
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



        adapter = TrackAdapter { track -> PlayerActivity(track) }
        binding.favoriteList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            binding.favoriteList.adapter = adapter
        }

        audioPlayerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                favouriteViewModel.updateFavorites()
            }
        }

        favouriteViewModel.updateFavorites()

        favouriteViewModel.observeState().observe(viewLifecycleOwner) {state ->
            render(state)
        }
    }




    private fun PlayerActivity(track: Track) {
        if (clickDebounce()) {
            val bundle = bundleOf(TRACK_KEY to track)
            findNavController().navigate(
                R.id.media_to_playlist, bundle
            )
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
        binding.placeholderImage.visibility = View.GONE
        binding.placeholderText.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        if (tracks.isNotEmpty()) {
            binding.favoriteList.visibility = View.VISIBLE
            adapter?.notifyDataSetChanged()
            updateFavoriteState(tracks)
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

    private fun updateFavoriteState(data: List<Track>) {
        adapter?.tracks = data.toMutableList()
        binding.favoriteList.adapter = adapter
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
        favouriteViewModel.updateFavorites()

    }

    override fun onResume() {
        super.onResume()
        favouriteViewModel.updateFavorites()

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