package com.practicum.playlist5.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist5.creator.Creator
import com.practicum.playlist5.R
import com.practicum.playlist5.domain.api.TrackInteractor
import com.practicum.playlist5.domain.api.SearchHistoryInteractor
import com.practicum.playlist5.domain.models.Track
import com.practicum.playlist5.ui.audio.AudioPlayerActivity

class SearchActivity : AppCompatActivity() {

    private val tracklist = mutableListOf<Track>()
    private lateinit var trackset: RecyclerView
    private lateinit var arrow: Button
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var errorMessage: LinearLayout
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var refreshButton: Button
    private lateinit var hintMessage: TextView
    private lateinit var historyList: RecyclerView
    private lateinit var clearHistory: Button
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var progressBar: ProgressBar
    private var text: String = ""

    private val searchHistoryInteractor: SearchHistoryInteractor by lazy {
        Creator.provideSearchHistoryInteractor()
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poisk)

        trackset = findViewById(R.id.recyclerview)
        trackset.layoutManager = LinearLayoutManager(this)
        arrow = findViewById(R.id.light_mode)
        inputEditText = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)
        errorMessage = findViewById(R.id.ErrorMessage)
        errorImage = findViewById(R.id.ErrorImage)
        errorText = findViewById(R.id.ErrorText)
        refreshButton = findViewById(R.id.RefreshButton)
        hintMessage = findViewById(R.id.hintMessage)
        historyList = findViewById(R.id.historyList)
        clearHistory = findViewById(R.id.buttonClearHistory)
        searchHistoryLayout = findViewById(R.id.searchHistoryLayout)
        progressBar = findViewById(R.id.progress_bar)

        clearButton.setOnClickListener {
            inputEditText.text.clear()
            tracklist.clear()
            historyList.isVisible = true
            clearButton.visibility = View.GONE
            hideKeyboard(inputEditText)
            historyMessage()
            searchAdapter.notifyDataSetChanged()
            loadSearchHistory()
        }

        arrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        fun clearButtonVisibility(s: CharSequence?): Int {
            return if (s.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        refreshButton.setOnClickListener {
            search()
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty() && searchHistoryInteractor.getTrackHistory().isNotEmpty()) {
                historyMessage()
            } else {
                trackset.isVisible = true
                errorMessage.isVisible = false
                searchHistoryLayout.isVisible = false
            }
        }

        clearHistory.setOnClickListener {
            searchHistoryInteractor.clearTrackHistory()
            historyAdapter.tracks.clear()
            historyAdapter.notifyDataSetChanged()
            searchHistoryLayout.isVisible = false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text = inputEditText.text.toString()
                clearButton.visibility = clearButtonVisibility(s)
                if (inputEditText.hasFocus() && s?.isEmpty() == true) {
                    historyMessage()
                } else {
                    searchDebounce()
                    trackset.isVisible = true
                    errorMessage.isVisible = false
                    searchHistoryLayout.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)


        val onHistoryTrackClickListener = TrackAdapter.ItemClickListener { track ->
            searchHistoryInteractor.addTrack(track) // Add this line to save history
            PlayerActivity(track)
        }

        historyAdapter = TrackAdapter(onHistoryTrackClickListener)
        historyList.layoutManager = LinearLayoutManager(this)
        historyList.adapter = historyAdapter





        val onItemClickListener = TrackAdapter.ItemClickListener { track ->
            searchHistoryInteractor.addTrack(track)
            searchHistoryLayout.isVisible = false
            PlayerActivity(track)
        }


        trackset.layoutManager = LinearLayoutManager(this)
        searchAdapter = TrackAdapter(onItemClickListener)
        searchAdapter.tracks = tracklist as ArrayList<Track>
        trackset.adapter = searchAdapter

        loadSearchHistory()


    }

    private fun loadSearchHistory() {
        val history = searchHistoryInteractor.getTrackHistory()
        historyAdapter.tracks = history.toMutableList() as ArrayList<Track>
        historyAdapter.notifyDataSetChanged()
        searchHistoryLayout.isVisible = historyAdapter.tracks.isNotEmpty()
    }


    private fun search() {
        if (inputEditText.text.isEmpty()) return

        if (!isNetworkAvailable()) {
            handleNetworkError()
            return
        }
        historyList.isVisible = false
        tracklist.clear()
        searchAdapter.notifyDataSetChanged()
        progressBar.visibility = View.VISIBLE


        Creator.provideTrackInteractor().search(inputEditText.text.toString(), object : TrackInteractor.TracksConsumer {
            override fun consume(foundTrack: List<Track>) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    tracklist.clear()
                    if (foundTrack.isNotEmpty()) {
                        refreshButton.isVisible = false
                        errorMessage.isVisible = false
                        searchHistoryLayout.isVisible = false
                        tracklist.addAll(foundTrack)
                        searchAdapter.notifyDataSetChanged()
                    } else {
                        errorMessage.isVisible = true
                        errorText.text = getString(R.string.nothing_found)
                        searchHistoryLayout.isVisible = false
                        errorImage.setImageResource(R.drawable.emodji_error)
                        refreshButton.isVisible = false
                    }
                }
            }

            override fun onFailure(t: Throwable) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    handleNetworkError()
                }
            }
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun handleNetworkError() {
        tracklist.clear()
        errorMessage.isVisible = true
        searchHistoryLayout.isVisible = false
        errorText.text = getString(R.string.something_wrong)
        errorImage.setImageResource(R.drawable.noconnection_error)
        refreshButton.isVisible = true
    }

    private fun historyMessage() {
        trackset.isVisible = false
        errorMessage.isVisible = false
        searchHistoryLayout.isVisible = historyAdapter.tracks.isNotEmpty()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT, text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        text = savedInstanceState.getString(INPUT, text)
        inputEditText.setText(text)
    }

    private fun PlayerActivity(track: Track) {
        Log.e("piss","piss")
        if (clickDebounce()) {
            val intent = Intent(this@SearchActivity,AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            startActivity(intent)
        }
    }

    companion object {
        const val INPUT = "INPUT"
        const val TRACK_KEY = "track"
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}



