package com.example.doubanmovie.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
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

class MovieTypeViewModel : ViewModel() {

    val movieTypes: LiveData<MutableList<String>> get() = _movieTypes
    private val _movieTypes = MutableLiveData<MutableList<String>>()
    private val movieTypeList = mutableListOf<String>()
    private val tagMovieType = "MainActivity MovieType"
    private var client: OkHttpClient
    private var builder: OkHttpClient.Builder
    private val getMovieTypeAddress =
        "https://movie.douban.com/j/search_tags?type=movie&tag=%E7%83%AD%E9%97%A8&source="

    init {
        builder = OkHttpClient.Builder()
        client = builder.build()
    }

    fun setMovieTypes() {
        _movieTypes.postValue(movieTypeList)
    }

    // 网络：获取电影种类
    fun getMovieTypeFromInternet() {
        val request = Request.Builder().url(getMovieTypeAddress).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected Code $response")
                    for ((name, value) in response.headers) {
                        Log.d(tagMovieType, "MovieType $name: $value")
                    }

                    Log.d(tagMovieType, response.toString())

                    var jsonData = response.body?.string() ?: ""
                    Log.d(tagMovieType, jsonData)
                    jsonData = jsonData.substringAfter(':')
                    jsonData = jsonData.substringBeforeLast('}')
                    Log.d(tagMovieType, jsonData)

                    val gson = GsonBuilder().create()
                    val type = object : TypeToken<List<String>>() {}.type
                    movieTypeList.addAll(gson.fromJson(jsonData, type))
                    Log.d(tagMovieType, "转换出的movieTypes有 ${movieTypeList.size} 个")
                    setMovieTypes()
                }
            }
        })
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
                setMovieTypes()
            }
        }
    }
}