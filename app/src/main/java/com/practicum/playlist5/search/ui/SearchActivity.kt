package com.practicum.playlist5.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlist5.R
import com.practicum.playlist5.audioplayer.ui.AudioPlayerActivity
import com.practicum.playlist5.databinding.ActivitySearchBinding
import com.practicum.playlist5.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private val viewModel by viewModel <SearchViewModel>()



    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupAdapters()

        observeViewModel()

        setupListeners()

    }

    private fun setupAdapters() {

        searchAdapter = TrackAdapter { track -> PlayerActivity(track) }
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }

        historyAdapter = TrackAdapter { track -> PlayerActivity(track) }
        binding.historyList.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = historyAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
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
        }
    }


    private fun setupListeners() {


        binding.lightMode.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.text.clear()
            hideKeyboard(binding.inputEditText)
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
                hideKeyboard(binding.inputEditText)
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

    private fun showTracks(tracks: List<Track>) {
        binding.progressBar.isVisible = false
        binding.recyclerview.isVisible = true
        binding.searchHistoryLayout.isVisible = false
        binding.buttonClearHistory.isVisible=false
        binding.recyclerview.adapter=searchAdapter
        searchAdapter.tracks= tracks as ArrayList<Track>
        searchAdapter.notifyDataSetChanged()

    }

    private fun showHistoryList(tracks: List<Track>) {
        binding.progressBar.isVisible = false
        binding.RefreshButton.isVisible = false
        binding.ErrorMessage.isVisible = false
        binding.searchHistoryLayout.isVisible = true
        binding.recyclerview.isVisible = false
        binding.historyList.layoutManager = LinearLayoutManager(this)
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






    private fun PlayerActivity(track: Track) {
        if (clickDebounce()) {
            viewModel.addTrack(track)
            val intent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            startActivity(intent)
        }
    }


    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            binding.root.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val TRACK_KEY = "track"
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}




