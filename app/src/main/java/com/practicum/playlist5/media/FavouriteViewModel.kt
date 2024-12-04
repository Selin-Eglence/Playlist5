package com.practicum.playlist5.media

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist5.R
import com.practicum.playlist5.media.domain.FavouriteInteractor
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavouriteViewModel(private val context: Context,
                         private val favouriteInteractor: FavouriteInteractor): ViewModel() {
    private val stateLiveData = MutableLiveData<FavouriteState>()
    fun observeState(): LiveData<FavouriteState> = stateLiveData


    private fun fillData() {
        viewModelScope.launch {
            renderState(FavouriteState.Loading)
            favouriteInteractor.getFavoriteTracks().collect { tracks ->
                if (tracks.isEmpty()) {
                    renderState(FavouriteState.Empty(context.getString(R.string.nothing_found)))
                } else {
                    renderState(FavouriteState.Content(tracks))
                }
            }
        }
    }


    fun updateFavorites() {
        fillData()
    }

    private fun renderState(state: FavouriteState) {
        stateLiveData.postValue(state)
    }

                         }