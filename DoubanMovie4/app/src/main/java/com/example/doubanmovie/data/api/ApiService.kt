package com.example.doubanmovie.data.api

import android.content.Context
import com.example.doubanmovie.data.model.Episode
import com.google.gson.GsonBuilder
import com.google.gson.internal.GsonBuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ApiService {
    suspend fun getEpisodes(): Call<List<Episode>>
    suspend fun getMovieTypesFromInternet(): List<String>
    suspend fun getMovieItemsFromInternet(movieTag: String?, pageStart: Int?): List<Episode>
    fun setMovieItemToFile(context: Context, movieTag: String, episodeList: List<Episode>)
    fun getMovieItemFromFile(context: Context, movieTag: String):List<Episode>
}