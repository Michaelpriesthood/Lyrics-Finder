package com.techmedia.lyricfinder.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.techmedia.lyricfinder.repository.LyricsRepository

class LyricsViewModelProviderFactory(val app: Application, private val lyricsRepository: LyricsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LyricsViewModel(app, lyricsRepository) as T
    }
}

