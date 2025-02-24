package com.practicum.playlist5.media.ui.playlist

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlist5.R
import com.practicum.playlist5.databinding.FragmentOnePlaylistBinding
import com.practicum.playlist5.search.domain.models.Track
import com.practicum.playlist5.search.ui.SearchFragment.Companion.TRACK_KEY
import com.practicum.playlist5.search.ui.TrackAdapter
import com.practicum.playlist5.search.ui.TrackViewHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnePlaylistFragment:Fragment() {

    private val onePlaylistViewModel: OnePlaylistViewModel by viewModel()
    private var _binding: FragmentOnePlaylistBinding? = null
    private val binding get()= _binding!!

    private lateinit var  trackAdapter: TrackAdapter

    private var isClickAllowed = true

    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    private var playlist: Playlist? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentOnePlaylistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackAdapter = TrackAdapter()


        binding.main.viewTreeObserver.addOnPreDrawListener(listener)

        binding.shareOnePlaylist.setOnClickListener {
            sharePlaylist()
        }

        val bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.menuBottomSheet)
        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN

        binding.menuOnePlaylist.setOnClickListener {

            _binding ?: return@setOnClickListener
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HALF_EXPANDED

            bottomSheetBehaviorMenu.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.isVisible = false
                        }

                        else -> {
                            binding.overlay.isVisible = true
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.overlay.alpha = (slideOffset + 1f) / 2f
                }
            })
        }


        val playlistId = arguments?.getLong(PlaylistFragment.PLAYLIST)
        if (playlistId != null) {
            onePlaylistViewModel.loadPlaylistById(playlistId)
        } else {
            findNavController().popBackStack()
        }


        onePlaylistViewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            this.playlist = playlist
            if (playlist != null) {
                setupUI(playlist)
            }
        }

        onePlaylistViewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            trackAdapter.tracks= tracks.toMutableList()
            if (tracks.isEmpty()) {
                Toast.makeText(requireContext(), "Плейлист пуст", Toast.LENGTH_LONG)
                    .show()
            }
            trackAdapter.notifyDataSetChanged()
        }

        binding.toolbarOnePlaylist.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        onePlaylistViewModel.totalDuration.observe(viewLifecycleOwner) { duration ->
            binding.minutesTv.text = duration
        }

        onePlaylistViewModel.trackCount.observe(viewLifecycleOwner) { count ->
            binding.countTrackTv.text = context?.getFormattedCount(count)
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = 0f
            }

        })


        binding.bottomSheetRecyclerViewTrack.layoutManager = LinearLayoutManager(requireContext())

        trackAdapter.onItemClickListener = TrackViewHolder.OnItemClickListener { track ->
            PlayerActivity(track)
        }

        trackAdapter.onItemLongClickListener = TrackViewHolder.OnItemLongClickListener { track ->
            showDeleteConfirmationDialogTrack(track)
            true
        }

        binding.bottomSheetRecyclerViewTrack.adapter = trackAdapter

        binding.playlistShare.setOnClickListener {
            sharePlaylist()
        }

        binding.playlistDelete.setOnClickListener {
            showDeleteConfirmationDialogPlaylist()
        }

        binding.playlistEditInformation.setOnClickListener {
            playlist?.let {
                navigateToEditPlaylist(it)
            }
        }




    }

    private val listener = ViewTreeObserver.OnPreDrawListener {
        _binding ?: return@OnPreDrawListener true
        bottomSheetBehavior?.peekHeight = binding.main.height - binding.onePlaylistFragment.height
        lifecycleScope.launch {
            delay(1000)
            removeOnPreDrawListener()
        }
        true
    }



    private fun navigateToEditPlaylist(playlist: Playlist) {
        val direction = OnePlaylistFragmentDirections.actionOnePlaylistFragmentToNewPlaylistFragment(playlist)
        findNavController().navigate(direction)
    }

    private fun sharePlaylist() {
        playlist?.let { playlist ->
            if (onePlaylistViewModel.tracks.value.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "В этом плейлисте нет списка треков, которым можно поделиться.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                var tracksList = ""

                val tracks = onePlaylistViewModel!!.tracks!!.value!!
                for (i in 0 until tracks.size) {
                    val track = tracks[i]
                    val duration = formatDuration(track.trackTimeMillis)
                    tracksList += "${i + 1}. ${track.artistName} - " +
                            "${track.trackName} ($duration)"
                    if (i < tracks.size - 1) {
                        tracksList += "\n"
                    }
                }

                val formattedTrackCount = requireContext().getFormattedCount(playlist.trackNum)

                val message = """
                |${playlist.name}
                |${playlist.description}
                |$formattedTrackCount 
                |$tracksList
            """.trimMargin("|")

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, message)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, "Поделиться плейлистом с"))
            }
        }
    }


    private fun showDeleteConfirmationDialogPlaylist() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить плейлист")
            .setMessage("Хотите удалить плейлист?")
            .setPositiveButton("ДА") { dialog, _ ->
                playlist?.let {

                    onePlaylistViewModel.removePlaylist(it)
                }
                dialog.dismiss()
                findNavController().navigateUp()
            }
            .setNegativeButton("НЕТ") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDeleteConfirmationDialogTrack(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить трек")
            .setMessage("Хотите удалить трек?")
            .setPositiveButton("ДА") { dialog, _ ->
                playlist?.let { onePlaylistViewModel.removeTrack(track.trackId, it.id) }
                dialog.dismiss()
            }
            .setNegativeButton("НЕТ") { dialog, _ ->
            }
            .show()
    }

    private fun setupUI(playlist: Playlist) {
        binding.nameOnePlaylist.text = playlist.name
        binding.descriptionOnePlaylist.text = playlist.description
        val trackCount = context?.getFormattedCount(playlist.trackNum)
        binding.countTrackTv.text = trackCount

        binding.namePlaylistBs.text = playlist.name
        binding.minutesAndTracksBs.text = playlist.trackNum.let { context?.getFormattedCount(it) }

        if (playlist.imagePath?.isNotEmpty() == true) {
            binding.artWorkOnePlaylist.setImageURI(Uri.parse(playlist.imagePath))
            binding.playlistCoverBs.setImageURI(Uri.parse(playlist.imagePath))

        }

    }

    private fun PlayerActivity(track: Track) {
        if (clickDebounce()) {
            val bundle = bundleOf(TRACK_KEY to track)
            findNavController().navigate(R.id.action_onePlaylistFragment_to_audioPlayerFragment, bundle)
        }
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

    override fun onResume() {
        super.onResume()
        isClickAllowed = true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    private fun removeOnPreDrawListener() {
        _binding ?: return
        binding.main.viewTreeObserver.removeOnPreDrawListener(listener)
    }

    fun formatDuration(durationMillis: Long): String {
        val seconds = (durationMillis / 1000) % 60
        val minutes = (durationMillis / 1000) / 60
        return String.format("%d:%02d", minutes, seconds)
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

    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }


}