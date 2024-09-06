package com.practicum.playlist5.data.repository


import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlist5.domain.models.Track
import com.practicum.playlist5.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(private val searchHistPref: SharedPreferences) :
    SearchHistoryRepository {

    override fun updateHistory() {
        val trackJson = searchHistPref.getString(KEY, null)
        if (trackJson != null) {
            getTrackHistory()

        }
    }

    override fun getTrackHistory(): List<Track> {
        val trackJson = searchHistPref.getString(KEY, null)
        return if (trackJson != null) {
            val typeToken = object : TypeToken<ArrayList<Track>>() {}.type
            Gson().fromJson(trackJson, typeToken)
        } else {
            emptyList()
        }
    }

    override fun addTrack(track: Track) {
        val tracks = getTrackHistory().toMutableList()
        tracks.removeAll { it.trackId == track.trackId }
        tracks.add(0, track)
        if (tracks.size > 10) {
            tracks.removeAt(tracks.size - 1)
        }
        val trackJson = Gson().toJson(tracks)
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
