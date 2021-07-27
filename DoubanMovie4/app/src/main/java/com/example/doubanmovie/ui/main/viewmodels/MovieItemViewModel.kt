package com.example.doubanmovie.ui.main.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doubanmovie.data.model.Episode
import com.example.doubanmovie.data.repository.MainRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MovieItemViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val episode: LiveData<MutableList<Episode>> get() = _episodes
    private val _episodes = MutableLiveData<MutableList<Episode>>()
    private var episodeList = mutableListOf<Episode>()
    private val tag = "MainActivity"

    init {
        _episodes.value = episodeList
    }

    private fun refresh() {
        _episodes.postValue(episodeList)
    }

    // 网络：获取电影列表
    fun getMovieItemsFromInternet(movieTag: String?, pageStart: Int?, clearAll: Boolean) {
        GlobalScope.launch {
            if (clearAll) episodeList.clear()
            episodeList.addAll(mainRepository.getMovieItemsFromInternet(movieTag, pageStart))
            refresh()
        }
    }

    // 本地：保存电影列表
    fun setMovieDataToFile(context: Context, movieTag: String) {
        mainRepository.setMovieDataToFile(context, movieTag, episodeList)
    }

    // 本地：获取电影列表
    fun getMovieDataFromFile(context: Context, movieTag: String) {
        episodeList = mainRepository.getMovieDataFromFile(context, movieTag) as MutableList<Episode>
        refresh()
    }

}