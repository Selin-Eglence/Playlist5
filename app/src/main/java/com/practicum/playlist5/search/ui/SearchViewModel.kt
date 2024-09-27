package com.practicum.playlist5.search.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist5.R
import com.practicum.playlist5.search.domain.api.SearchHistoryInteractor
import com.practicum.playlist5.search.domain.api.SearchHistoryRepository
import com.practicum.playlist5.search.domain.api.TrackInteractor
import com.practicum.playlist5.search.domain.models.Track
import com.practicum.playlist5.utils.Creator

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val trackInteractor: TrackInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { latestSearchText?.let { search(it) } }

    private val tracklist = mutableListOf<Track>()


    private val _stateLiveData = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> get() = _stateLiveData

    private val _trackHistory = MutableLiveData<List<Track>>()
    val trackHistory: LiveData<List<Track>> get() = _trackHistory

    private var latestSearchText: String? = null

    init {
        Log.d("TEST", "ViewModel initialized!")
    }


    private fun renderState(state: SearchState) {
        _stateLiveData.postValue(state)
    }


    fun searchDebounce(changedText: String) {
        latestSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }







    fun search(newSearchText: String) {
        Log.d("mistake", "search")
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)



            trackInteractor.search(newSearchText, object : TrackInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>) {
                    tracklist.clear()
                    if (foundTracks.isNotEmpty()) {
                        Log.d("mistake", "result")
                        tracklist.addAll(foundTracks)
                        renderState(SearchState.SearchList(tracklist))
                    } else {
                        Log.e("mistake", "empty")
                        renderState(SearchState.NothingFound(emptyMessage = "Nothing was Found"))
                    }
                }

                override fun onFailure(t: Throwable) {
                    Log.e("mistake", "error", t)
                    renderState(SearchState.Error(errorMessage = "Something went wrong. Please try again."))
                }
            })
        }
    }





    fun addTrack(track: Track) {
        searchHistoryInteractor.addTrack(track)
    }


    fun clearHistory() {
        searchHistoryInteractor.clearTrackHistory()
        renderState(SearchState.HistoryList(emptyList()))
    }

    fun loadSearchHistory() {
        val historyTracks = searchHistoryInteractor.getTrackHistory()
        renderState(SearchState.HistoryList(historyTracks))
        _trackHistory.value = historyTracks
    }



    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    searchHistoryInteractor = Creator.provideSearchHistoryInteractor(),
                    trackInteractor = Creator.provideTrackInteractor()
                )
            }
        }
    }
}





