package com.example.doubanmovie.data.api

import android.content.Context
import com.example.doubanmovie.data.model.Episode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext.Element

class ApiHelper(private val apiService: ApiService) {
    suspend fun getMovieTypesFromInternet(): List<String> = apiService.getMovieTypesFromInternet()
    suspend fun  getMovieItemsFromInternet(movieTag: String?, pageStart: Int?):List<Episode> = apiService.getMovieItemsFromInternet(movieTag, pageStart)
    fun setMovieDataToFile(context: Context, movieTag: String, episodeList: List<Episode>) = apiService.setMovieDataToDisk(context, movieTag, episodeList)
    fun getMovieDataFromFile(context: Context, movieTag: String):List<Episode> = apiService.getMovieDataFromDisk(context, movieTag)
}