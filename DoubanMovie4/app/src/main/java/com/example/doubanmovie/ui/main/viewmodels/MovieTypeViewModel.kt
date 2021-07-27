package com.example.doubanmovie.ui.main.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doubanmovie.data.repository.MainRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class MovieTypeViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val movieTypes: LiveData<MutableList<String>> get() = _movieTypes
    private val _movieTypes = MutableLiveData<MutableList<String>>()
    private val movieTypeList = mutableListOf<String>()
    private val tagMovieType = "MainActivity MovieType"
    private var client: OkHttpClient
    private var builder: OkHttpClient.Builder = OkHttpClient.Builder()

    init {
        client = builder.build()
    }

    fun refresh() {
        _movieTypes.postValue(movieTypeList)
    }

    // 网络：获取电影种类
    fun getMovieTypeFromInternet(context: Context) {
        GlobalScope.launch {
            mainRepository.getMovieTypesFromInternet().also {
                Log.d(tagMovieType, "添加到movieTypeList ${it.size}个")
                movieTypeList.clear()
                movieTypeList.addAll(it)
                setMovieTypeToFile(context)
                refresh()
            }
        }
    }

    // 本地：保存电影种类数据到
    private fun setMovieTypeToFile(context: Context) {
        mainRepository.setMovieTypeToFile(context, movieTypeList)
    }

    // 本地：获取电影种类
    fun getMovieTypeFromFile(context: Context) {
        mainRepository.getMovieTypeFromFile(context, movieTypeList)
        refresh()
    }
}