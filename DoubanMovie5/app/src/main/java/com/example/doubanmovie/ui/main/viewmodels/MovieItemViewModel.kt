package com.example.doubanmovie.ui.main.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doubanmovie.data.model.Episode
import com.example.doubanmovie.data.repository.MainRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    fun clearAll() {
        episodeList.clear()
        refresh()
    }

    // 网络：获取电影列表
    fun getMovieItemsFromInternet(movieTag: String?, pageStart: Int?, clearAll: Boolean, save: Boolean, context: Context) {
        viewModelScope.launch {
            if (clearAll) episodeList.clear()
            mainRepository.getMovieItemsFromInternet(movieTag, pageStart).let {
                episodeList.addAll(it)
                Log.d(tag, "从网络获取到${it.size}个")
            }
            refresh()
            if (save) {
                setMovieDataToFile(context, movieTag ?: "热门")
            }
        }
    }

    // 本地：保存电影列表
    fun setMovieDataToFile(context: Context, movieTag: String) {
        viewModelScope.launch {
            Log.d(tag, "保存到本地 ${episodeList.size}个 1")
            mainRepository.setMovieItemToFile(context, movieTag, episodeList)
        }
    }

    // 本地：获取电影列表
    fun getMovieDataFromFile(context: Context, movieTag: String) {
        viewModelScope.launch {
            val temp = mainRepository.getMovieItemFromFile(context, movieTag)
            (mainRepository.getMovieItemFromFile(context, movieTag) as MutableList<Episode>).also {
                Log.d(tag + "HAHA", it.toString())
                episodeList = it
            }
            refresh()
        }
    }

}