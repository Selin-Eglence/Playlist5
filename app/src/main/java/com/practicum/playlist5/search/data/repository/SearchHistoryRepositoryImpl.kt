package com.practicum.playlist5.search.data.repository


import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlist5.search.domain.models.Track
import com.practicum.playlist5.search.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(private val searchHistPref: SharedPreferences,
    private val gson: Gson) :
    SearchHistoryRepository {

    override fun updateHistory() {
        val trackJson = searchHistPref.getString(KEY, null)
        if (trackJson != null) {
            getTrackHistory()

        }
    }

    override fun getTrackHistory(): MutableList<Track> {
        val trackJson = searchHistPref.getString(KEY, null)
        return if (trackJson != null) {
            val typeToken = object : TypeToken<ArrayList<Track>>() {}.type
            gson.fromJson(trackJson, typeToken)
        } else {
            mutableListOf()
        }
    }

    override fun addTrack(track: Track) {
        val tracks = getTrackHistory().toMutableList()
        tracks.removeAll { it.trackId == track.trackId }
        tracks.add(0, track)
        if (tracks.size > 10) {
            tracks.removeAt(tracks.size - 1)
        }
        val trackJson = gson.toJson(tracks)
        searchHistPref.edit().putString(KEY, trackJson).apply()
        updateHistory()
    }

    override fun clearTrackHistory() {
        searchHistPref.edit().remove(KEY).apply()

    }


    companion object {
        const val KEY = "tracks"
    }
}
