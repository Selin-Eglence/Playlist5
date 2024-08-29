package com.practicum.playlist5

import android.content.Context
import com.google.gson.Gson
import android.content.SharedPreferences
import com.google.gson.reflect.TypeToken
import com.practicum.playlist5.domain.models.Track
import com.practicum.playlist5.presentation.search.TrackAdapter

class SearchHistory(private val context: Context, private val adapter: TrackAdapter) {
    private val searchHistPref: SharedPreferences =
        context.getSharedPreferences(KEY, Context.MODE_PRIVATE)

    init {
        updateHistory()
        searchHistPref.registerOnSharedPreferenceChangeListener { sharedPreferences, s ->
            if (s == KEY) {
                updateHistory()
            }
        }
    }

    fun updateHistory() {
        val trackJson = searchHistPref.getString(KEY, null)
        if (trackJson != null) {
            val trackNew = getTrackHistory()
            adapter.tracks.clear()
            adapter.tracks.addAll(trackNew)
            adapter.notifyDataSetChanged()
        }
    }

    fun getTrackHistory(): List<Track> {
        val trackJson = searchHistPref.getString(KEY, null)
        return if (trackJson != null) {
            val typeToken = object : TypeToken<ArrayList<Track>>() {}.type
            Gson().fromJson(trackJson, typeToken)
        } else {
            emptyList()
        }
    }

    fun addTrack(track: Track) {
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

    fun clearTrackHistory() {
        searchHistPref.edit().remove(KEY).apply()
        adapter.tracks.clear()
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val KEY = "tracks"
    }
}
