package com.techmedia.lyricfinder.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.techmedia.lyricfinder.model.LyricsModel

@Dao
interface LyricsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(lyrics: LyricsModel): Long

    @Query("SELECT * FROM lyrics ORDER BY id DESC")
    fun getAllLyrics(): LiveData<List<LyricsModel>>

    @Delete
    suspend fun deleteLyrics(lyrics: LyricsModel)
}
