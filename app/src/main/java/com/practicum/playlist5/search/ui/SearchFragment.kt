package com.practicum.playlist5.search.ui

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlist5.R
import com.practicum.playlist5.audioplayer.ui.AudioPlayerActivity
import com.practicum.playlist5.databinding.FragmentSearchBinding
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private val viewModel by viewModel <SearchViewModel>()



    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()

        observeViewModel()

        setupListeners()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




    private fun setupAdapters() {

        searchAdapter = TrackAdapter { track -> PlayerActivity(track) }
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }

        historyAdapter = TrackAdapter { track -> PlayerActivity(track) }
        binding.historyList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
    }


    private fun renderState(state: SearchState) {
        when (state) {
            is SearchState.SearchList -> showTracks(state.tracks)
            is SearchState.HistoryList -> showHistoryList(state.tracks)
            is SearchState.Error -> showErrorMessage()
            is SearchState.Loading -> showLoading()
            is SearchState.NothingFound -> showEmptyView()
            is SearchState.NoHistory ->showHistoryMessage()
        }
    }


    private fun setupListeners() {



        binding.clearIcon.setOnClickListener {
            binding.inputEditText.text.clear()
            hideKeyboard()
            binding.recyclerview.isVisible=false
            binding.historyList.isVisible = true
            binding.clearIcon.visibility = View.GONE
            searchAdapter.notifyDataSetChanged()
            viewModel.loadSearchHistory()
            }


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                viewModel.searchDebounce(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        binding.inputEditText.addTextChangedListener(textWatcher)


        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.search(binding.inputEditText.text.toString())
                hideKeyboard()
            }
            false
        }


        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.RefreshButton.setOnClickListener {
            viewModel.search(binding.inputEditText.text.toString())
        }


    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.recyclerview.isVisible = false
        binding.historyList.isVisible = false
        binding.ErrorMessage.isVisible = false
    }

    private fun showTracks(tracks: MutableList<Track>) {
        binding.progressBar.isVisible = false
        binding.recyclerview.isVisible = true
        binding.searchHistoryLayout.isVisible = false
        binding.buttonClearHistory.isVisible=false
        binding.recyclerview.adapter=searchAdapter
        searchAdapter.tracks= tracks as ArrayList<Track>
        searchAdapter.notifyDataSetChanged()

    }

    private fun showHistoryList(tracks: MutableList<Track>) {
        binding.progressBar.isVisible = false
        binding.RefreshButton.isVisible = false
        binding.ErrorMessage.isVisible = false
        binding.searchHistoryLayout.isVisible = true
        binding.recyclerview.isVisible = false
        binding.historyList.layoutManager = LinearLayoutManager(requireContext())
        binding.historyList.adapter = historyAdapter
        binding.buttonClearHistory.isVisible=true
        historyAdapter.tracks = tracks.toMutableList() as ArrayList<Track>
        historyAdapter.notifyDataSetChanged()
        binding.searchHistoryLayout.isVisible = historyAdapter.tracks.isNotEmpty()
    }

    private fun showEmptyView() {
        binding.progressBar.isVisible = false
        binding.ErrorMessage.isVisible = true
        binding.ErrorText.text = getString(R.string.nothing_found)
        binding.searchHistoryLayout.isVisible = false
        binding.recyclerview.isVisible = false
        binding.ErrorImage.setImageResource(R.drawable.emodji_error)
    }

    private fun showErrorMessage() {
        binding.ErrorMessage.isVisible = true
        binding.ErrorText.text = getString(R.string.something_wrong)
        binding.progressBar.isVisible = false
        binding.searchHistoryLayout.isVisible = false
        binding.recyclerview.isVisible = false
        binding.RefreshButton.isVisible=true
        binding.ErrorImage.setImageResource(R.drawable.noconnection_error)
    }


    private fun showHistoryMessage(){
        binding.recyclerview.isVisible = false
        binding.ErrorMessage.isVisible = false
        binding.searchHistoryLayout.isVisible = false
        binding.progressBar.isVisible = false
    }



    private fun PlayerActivity(track: Track) {
        if (clickDebounce()) {
            viewModel.addTrack(track)
            val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            startActivity(intent)
        }
    }


    private fun hideKeyboard() {
        val view = requireActivity().currentFocus
        if(view!=null){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }}


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch{
                delay(CLICK_DEBOUNCE_DELAY)
                        isClickAllowed= true
            }
        }
        return current
    }



    companion object {
        const val TRACK_KEY = "track"
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}




