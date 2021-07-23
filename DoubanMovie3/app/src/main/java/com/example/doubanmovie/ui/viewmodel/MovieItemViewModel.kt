package com.example.doubanmovie.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doubanmovie.logic.model.Episode
import com.example.doubanmovie.logic.model.SubjectBox
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MovieItemViewModel : ViewModel() {

    val episode: LiveData<MutableList<Episode>> get() = _episodes
    private val _episodes = MutableLiveData<MutableList<Episode>>()

    private val episodeList = ArrayList<Episode>()


    private var client: OkHttpClient
    private var builder: OkHttpClient.Builder

    private val getMovieAddress1 = "https://movie.douban.com/j/search_subjects?type=movie&tag="
    private val getMovieAddress2 = "&sort=recommend&page_limit=20&page_start="

    private val tag = "MainActivity"

    init {
        _episodes.value = mutableListOf() // OkHttp
        builder = OkHttpClient.Builder()
        client = builder.build()
    }

    private fun refresh() {
        _episodes.postValue(episodeList)
    }

    fun addEpisodes(episodes: MutableList<Episode>) {
        episodeList.addAll(episodes)
        _episodes.postValue(episodeList)
    }

    fun clearEpisodes() {
        episodeList.clear()
        _episodes.postValue(episodeList)
    }

    // 网络：获取电影列表
    fun getMovieItemsFromInternet(movieTag: String?, pageStart: Int?, clearAll: Boolean) {

        Log.d(tag, "############## getMovieItems ###########")
        Log.d(tag, "movieTag: $movieTag --- pageStart: $pageStart")
        Log.d(tag, movieTag.toString())
        val movieTag2 = movieTag ?: "热门"
        var pageStartString = "0"
        pageStart?.let {
            pageStartString = pageStart.toString()
        }

        val request =
            Request.Builder().url(getMovieAddress1 + movieTag2 + getMovieAddress2 + pageStartString)
                .build()
        Log.d(tag, getMovieAddress1 + movieTag2 + getMovieAddress2)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected Code $response")
                    for ((name, value) in response.headers) {
                        Log.d(tag, "$name : $value")
                    }

                    var jsonData = ""

                    Log.d(tag, response.toString())
                    response.body?.string()?.let { it1 ->
                        Log.d(tag, it1)
                        jsonData = it1
                    }

                    val gson = GsonBuilder().create()
                    val subjectBox = gson.fromJson(jsonData, SubjectBox::class.java).also {
                        for (i in it.subjects) {
                            Log.d(tag, i.title)
                        }
                    }
                    if (clearAll) {
                        clearEpisodes()
                    }
                    addEpisodes(ArrayList(subjectBox.subjects))
                }
            }
        })
    }

    // 本地：保存电影列表
    fun setMovieDataToDisk(context: Context, movieTag: String) {
        Log.d(tag, "保存数据 -- $movieTag -- ${episodeList.size}条")
        val cacheParent = File(context.cacheDir.path + "/movieItem").also {
            if (!it.exists()) it.mkdir()
        }
        val cache = File(cacheParent, movieTag)
        val fileOutputStream = FileOutputStream(cache)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)
        objectOutputStream.writeObject(episodeList)
        fileOutputStream.close()
        objectOutputStream.close()
    }

    // 本地：获取电影列表
    fun getMovieDataFromDisk(context: Context, movieTag: String) {
        Log.d(tag, "本地获取数据 -- movie_$movieTag")
        File("${context.cacheDir.path}/movieItem/$movieTag").let {
            if (it.exists()) {
                val fileInputStream = FileInputStream(it)
                val objectInputStream = ObjectInputStream(fileInputStream)
                @Suppress("UNCHECKED_CAST") episodeList.addAll(objectInputStream.readObject() as List<Episode>)
                Log.d(tag, "本地数据存在 -- $movieTag -- ${episodeList.size}条")
                refresh()
            }
        }
    }

}