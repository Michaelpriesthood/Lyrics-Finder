package com.techmedia.lyricfinder.repository

import com.techmedia.lyricfinder.api.RetrofitInstance
import com.techmedia.lyricfinder.db.LyricsDatabase
import com.techmedia.lyricfinder.model.LyricsModel

class LyricsRepository(private val db: LyricsDatabase) {
    suspend fun getLyrics(artistName: String, songTitle: String) =
        RetrofitInstance.api.getLyricsData(artistName, songTitle)

    suspend fun upsert(lyrics: LyricsModel) = db.getLyricsDao().upsert(lyrics)

    fun getSavedLyrics() = db.getLyricsDao().getAllLyrics()
    suspend fun deleteLyrics(lyrics: LyricsModel) = db.getLyricsDao().deleteLyrics(lyrics)

}