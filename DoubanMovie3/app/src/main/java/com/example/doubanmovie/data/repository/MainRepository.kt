package com.example.doubanmovie.data.repository

import android.content.Context
import com.example.doubanmovie.data.api.ApiHelper
import com.example.doubanmovie.data.model.Episode

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getMovieTypesFromInternet(): List<String> = apiHelper.getMovieTypesFromInternet()
    suspend fun getMovieItemsFromInternet(movieTag: String?, pageStart: Int?): List<Episode> = apiHelper.getMovieItemsFromInternet(movieTag, pageStart)
    fun setMovieDataToFile(context: Context, movieTag: String, episodeList: List<Episode>) = apiHelper.setMovieDataToFile(context, movieTag, episodeList)
    fun getMovieDataFromFile(context: Context, movieTag: String):List<Episode> = apiHelper.getMovieDataFromFile(context, movieTag)
}