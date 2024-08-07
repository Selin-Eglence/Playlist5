package com.practicum.playlist5

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast

class SearchActivity : AppCompatActivity() {

    private val ItunesBaseUrl = "https://itunes.apple.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(ItunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TrackAPI::class.java)
    private val tracklist = ArrayList<Track>()

    private lateinit var trackset: RecyclerView
    private lateinit var arrow: Button
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var errorMessage: LinearLayout
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var refreshButton: Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var hintMessage: TextView
    private lateinit var historyList: RecyclerView
    private lateinit var clearHistory: Button
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private var text: String = ""

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

        clearButton.setOnClickListener {
            inputEditText.text.clear()
            tracklist.clear()
            historyList.isVisible= true
            clearButton.visibility = View.GONE
            searchAdapter.notifyDataSetChanged()
            hideKeyboard(inputEditText)
            historyMessage()
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
            if (hasFocus && inputEditText.text.isEmpty() && searchHistory.getTrackHistory().isNotEmpty()) {
                historyMessage()
            } else {
                trackset.isVisible = true
                errorMessage.isVisible = false
                searchHistoryLayout.isVisible = false
            }
        }

        clearHistory.setOnClickListener {
            searchHistory.clearTrackHistory()
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
            Toast.makeText(
                this@SearchActivity,
                "Track: " + track.artistName + " - " + track.trackName,
                Toast.LENGTH_SHORT
            ).show()
        }
        historyAdapter = TrackAdapter(onHistoryTrackClickListener)
        historyList.layoutManager = LinearLayoutManager(this)
        historyList.adapter = historyAdapter

        searchHistory = SearchHistory(this, historyAdapter)

        val onItemClickListener = TrackAdapter.ItemClickListener { track ->
            searchHistory.addTrack(track)
            searchHistoryLayout.isVisible = false
        }

        trackset.layoutManager = LinearLayoutManager(this)
        searchAdapter = TrackAdapter(onItemClickListener)
        searchAdapter.tracks = tracklist
        trackset.adapter = searchAdapter

        // Load history on start
        searchHistory.updateHistory()
    }

    private fun search() {
        if (inputEditText.text.isEmpty()) return
        historyList.isVisible = false
        tracklist.clear()
        searchAdapter.notifyDataSetChanged()
        trackService.search(inputEditText.text.toString())
    .enqueue(object : Callback<TrackResponse> {
        override fun onResponse(
            call: Call<TrackResponse>,
            response: Response<TrackResponse>
        ) {
            Log.d(TAG, "onResponse: Received response with code ${response.code()}")
            if (response.code() == 200) {
                    tracklist.clear()

                    if (response.body()?.results?.isNotEmpty() == true) {
                        Log.d(TAG, "onResponse: Found ${response.body()?.results!!.size} tracks")
                        tracklist.clear()
                        refreshButton.isVisible = false
                        errorMessage.isVisible = false
                        searchHistoryLayout.isVisible = false
                        tracklist.addAll(response.body()?.results!!)
                        searchAdapter.notifyDataSetChanged()

                    }
                    if (tracklist.isEmpty()) {
                        Log.d(TAG, "onResponse: No tracks found")
                        tracklist.clear()
                        searchAdapter.notifyDataSetChanged()
                        errorMessage.isVisible = true
                        refreshButton.isVisible = false
                        errorText.text = getString(R.string.nothing_found)
                        searchHistoryLayout.isVisible = false
                        errorImage.setImageResource(R.drawable.emodji_error)
                    }
                }
                else {
                handleNetworkError()
                }
            }
        override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
           handleNetworkError()
        }
    })
    }


    private fun historyMessage() {
        trackset.isVisible = false
        errorMessage.isVisible= false
        searchHistoryLayout.isVisible= historyAdapter.tracks.isNotEmpty()
    }


    private fun handleNetworkError() {
        errorMessage.isVisible = true
        searchHistoryLayout.isVisible = false
        errorText.text = getString(R.string.something_wrong)
        errorImage.setImageResource(R.drawable.noconnection_error)
        refreshButton.isVisible = true
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

    companion object {
        const val INPUT = "INPUT"
    }

    }


