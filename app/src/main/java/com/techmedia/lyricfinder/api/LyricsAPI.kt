package com.techmedia.lyricfinder.api

import com.techmedia.lyricfinder.model.LyricsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LyricsAPI {

    @GET("v1/{artist}/{title}")
    suspend fun getLyricsData(
        @Path("artist") artistName: String,
        @Path("title") songTitle: String
    ): Response<LyricsModel>


}