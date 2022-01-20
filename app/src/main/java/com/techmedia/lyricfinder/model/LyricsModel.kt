package com.techmedia.lyricfinder.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "lyrics")
data class LyricsModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var lyrics: String? = null,
    var error: String? = null,
):Serializable