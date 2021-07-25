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
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

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
    fun getMovieTypeFromInternet() {
        GlobalScope.launch {
            mainRepository.getMovieTypesFromInternet().also {
                Log.d(tagMovieType, "添加到movieTypeList ${it.size}个")
                movieTypeList.addAll(it)
                refresh()
            }
        }
    }

    // 本地：保存电影种类数据到
    fun saveMovieTypeToDisk(context: Context) {
        Log.d(tagMovieType, "保存电影种类数据 -- ${movieTypeList.size}")
        val cacheParent = File(context.cacheDir.path.toString() + "/movieType").also {
            if (!it.exists()) it.mkdir()
        }
        val cache = File(cacheParent, "Tags")
        val fileOutputStream = FileOutputStream(cache)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)
        objectOutputStream.writeObject(movieTypeList)
        fileOutputStream.close()
        objectOutputStream.close()
    }

    // 本地：获取电影种类
    fun getMovieTypeData(context: Context) {
        File("${context.cacheDir.path}/movieType/Tags").let {
            if (it.exists()) {
                val fileInputStream = FileInputStream(it)
                val objectInputStream = ObjectInputStream(fileInputStream)
                @Suppress("UNCHECKED_CAST") movieTypeList.addAll(objectInputStream.readObject() as List<String>)
                refresh()
            }
        }
    }
}