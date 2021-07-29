package com.example.doubanmovie.data.api

import android.content.Context
import com.example.doubanmovie.data.model.Episode

class ApiHelper(private val apiService: ApiService) {
    suspend fun getMovieTypesFromInternet(): List<String> = apiService.getMovieTypesFromInternet()
    suspend fun  getMovieItemsFromInternet(movieTag: String?, pageStart: Int?):List<Episode> = apiService.getMovieItemsFromInternet(movieTag, pageStart)
    suspend fun setMovieDataToFile(context: Context, movieTag: String, episodeList: MutableList<Episode>) = apiService.setMovieItemToFile(context, movieTag, episodeList)
    suspend fun getMovieDataFromFile(context: Context, movieTag: String):MutableList<Episode> = apiService.getMovieItemFromFile(context, movieTag)
}