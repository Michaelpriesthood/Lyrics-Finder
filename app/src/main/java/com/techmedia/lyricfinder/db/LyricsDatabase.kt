package com.techmedia.lyricfinder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.techmedia.lyricfinder.model.LyricsModel

@Database(
    entities = [LyricsModel::class],
    version = 2,
    exportSchema = false
)


abstract class LyricsDatabase: RoomDatabase() {

    abstract fun getLyricsDao(): LyricsDao

    companion object {
        @Volatile
        private var instance: LyricsDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, LyricsDatabase::class.java, "lyrics_db.db"
        ).fallbackToDestructiveMigration().build()
    }

}