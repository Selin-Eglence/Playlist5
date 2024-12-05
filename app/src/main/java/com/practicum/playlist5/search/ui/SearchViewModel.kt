package com.practicum.playlist5.search.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist5.search.domain.api.SearchHistoryInteractor
import com.practicum.playlist5.search.domain.api.TrackInteractor
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val trackInteractor: TrackInteractor
) : ViewModel() {


    private val tracklist = mutableListOf<Track>()


    private val _stateLiveData = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> get() = _stateLiveData

    private val _trackHistory = MutableLiveData<List<Track>>()
    val trackHistory: LiveData<List<Track>> get() = _trackHistory

    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    init {
        Log.d("TEST", "ViewModel initialized!")
    }


    private fun renderState(state: SearchState) {
        _stateLiveData.postValue(state)
    }


    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            search(changedText)
        }


    }

    fun search(newSearchText: String) {
        Log.d("mistake", "search")
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)
            viewModelScope.launch {
                trackInteractor.search(newSearchText)
                    .collect { (foundTracks, errorMessage) ->
                        tracklist.clear()
                            if (foundTracks != null) {
                                tracklist.addAll(foundTracks)
                            }

                        when {
                            errorMessage != null -> {
                                Log.e("mistake", "error")
                                renderState(SearchState.Error(errorMessage = "Something went wrong. Please try again."))

                            }

                            tracklist.isEmpty() -> {
                                Log.e("mistake", "empty")
                                renderState(SearchState.NothingFound(emptyMessage = "Nothing was Found"))
                            }
                            else -> {
                                Log.d("mistake", "result")
                                renderState(SearchState.SearchList(tracklist))
                            }

                        }
                    }
            }


        }
    }


    fun addTrack(track: Track) {
        searchHistoryInteractor.addTrack(track)
    }


    fun clearHistory() {
        searchHistoryInteractor.clearTrackHistory()
        renderState(SearchState.HistoryList(mutableListOf()))
    }

    fun loadSearchHistory() {
        val historyTracks = searchHistoryInteractor.getTrackHistory()
        renderState(SearchState.HistoryList(historyTracks))
        _trackHistory.value = historyTracks
    }


    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}






