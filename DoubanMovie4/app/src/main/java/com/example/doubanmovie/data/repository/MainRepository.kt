package com.example.doubanmovie.data.repository

import android.content.Context
import android.util.Log
import com.example.doubanmovie.data.api.ApiHelper
import com.example.doubanmovie.data.model.Episode
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainRepository(private val apiHelper: ApiHelper) {
    private val tag = "MainActivity mainRepository"

    suspend fun getMovieTypesFromInternet(): List<String> = apiHelper.getMovieTypesFromInternet()
    suspend fun getMovieItemsFromInternet(movieTag: String?, pageStart: Int?): List<Episode> = apiHelper.getMovieItemsFromInternet(movieTag, pageStart)
    fun setMovieItemToFile(context: Context, movieTag: String, episodeList: List<Episode>) = apiHelper.setMovieDataToFile(context, movieTag, episodeList)
    fun getMovieItemFromFile(context: Context, movieTag: String):List<Episode> = apiHelper.getMovieDataFromFile(context, movieTag)
    fun setMovieTypeToFile(context: Context, movieTypeList: MutableList<String>) {
        Log.d(tag, "保存到本地电影种类数据 -- ${movieTypeList.size}")
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
    fun getMovieTypeFromFile(context: Context, movieTypeList: MutableList<String>) {
        File("${context.cacheDir.path}/movieType/Tags").let {
            if (it.exists()) {
                val fileInputStream = FileInputStream(it)
                val objectInputStream = ObjectInputStream(fileInputStream)
                @Suppress("UNCHECKED_CAST") movieTypeList.addAll(objectInputStream.readObject() as List<String>)
                Log.d(tag, "本地获取到movieType ${movieTypeList.size} 个")
            }
        }
    }
}