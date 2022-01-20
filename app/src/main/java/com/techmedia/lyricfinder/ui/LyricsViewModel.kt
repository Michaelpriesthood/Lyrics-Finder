package com.techmedia.lyricfinder.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techmedia.lyricfinder.LyricsApplication
import com.techmedia.lyricfinder.model.LyricsModel
import com.techmedia.lyricfinder.repository.LyricsRepository
import com.techmedia.lyricfinder.util.ScreenState
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class LyricsViewModel(app: Application, private val lyricsRepository: LyricsRepository) :
    AndroidViewModel(app) {


    private var _lyricsLiveData: MutableLiveData<ScreenState<LyricsModel>> = MutableLiveData()
    val lyricsLiveData: LiveData<ScreenState<LyricsModel>>
        get() = _lyricsLiveData

    //    get Lyrics from our api
    fun getLyricsData(artistName: String, songTitle: String) = viewModelScope.launch {
        _lyricsLiveData.postValue(ScreenState.Loading())
        safeGetLyricsCall(artistName, songTitle)

    }

    private fun handleResponse(response: Response<LyricsModel>): ScreenState<LyricsModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ScreenState.Success(resultResponse)
            }
        }

        return ScreenState.Error(response.message())
    }

    private suspend fun safeGetLyricsCall(artistName: String, songTitle: String) {
        _lyricsLiveData.postValue(ScreenState.Loading())

        try {
            if (isNetworkConnected()) {
                val response = lyricsRepository.getLyrics(artistName, songTitle)
                _lyricsLiveData.postValue(handleResponse(response))
            } else {
                _lyricsLiveData.postValue(ScreenState.Error("No internet connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _lyricsLiveData.postValue(ScreenState.Error("Network Failure"))
                else -> _lyricsLiveData.postValue(ScreenState.Error("Conversion Error"))
            }
        }
    }


    //    Save Lyrics Logic
    fun saveLyrics(lyrics: LyricsModel) = viewModelScope.launch {
        lyricsRepository.upsert(lyrics)
    }

    fun getSavedLyrics() = lyricsRepository.getSavedLyrics()

    fun deleteLyrics(lyrics: LyricsModel) = viewModelScope.launch {
        lyricsRepository.deleteLyrics(lyrics)
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getApplication<LyricsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }

}