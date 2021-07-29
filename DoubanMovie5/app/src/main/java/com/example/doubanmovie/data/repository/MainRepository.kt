package com.example.doubanmovie.data.repository

import android.content.Context
import android.util.Log
import com.example.doubanmovie.data.api.ApiHelper
import com.example.doubanmovie.data.model.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainRepository(private val apiHelper: ApiHelper) {
    private val tag = "MainActivity mainRepository"

    suspend fun getMovieTypesFromInternet(): List<String> = apiHelper.getMovieTypesFromInternet()
    suspend fun getMovieItemsFromInternet(movieTag: String?, pageStart: Int?): List<Episode> =
        apiHelper.getMovieItemsFromInternet(movieTag, pageStart)

    suspend fun setMovieItemToFile(context: Context, movieTag: String, episodeList: MutableList<Episode>) =
        apiHelper.setMovieDataToFile(context, movieTag, episodeList)

    suspend fun getMovieItemFromFile(context: Context, movieTag: String): List<Episode> =
        apiHelper.getMovieDataFromFile(context, movieTag)

    suspend fun setMovieTypeToFile(context: Context, movieTypeList: MutableList<String>) {
        withContext(Dispatchers.IO) {
            Log.d(tag, "保存到本地电影种类数据 -- ${movieTypeList.size}")
            val cacheParent = File(context.cacheDir.path.toString() + "/movieType").also {
                if (!it.exists()) it.mkdir()
            }
            val cache = File(cacheParent, "Tags")
            val fileOutputStream = FileOutputStream(cache)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            try {
                objectOutputStream.writeObject(movieTypeList)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                fileOutputStream.close()
                objectOutputStream.close()
            }
        }
    }

    suspend fun getMovieTypeFromFile(context: Context, movieTypeList: MutableList<String>) {
        withContext(Dispatchers.IO) {
            File("${context.cacheDir.path}/movieType/Tags").let {
                if (it.exists()) {
                    val fileInputStream = FileInputStream(it)
                    val objectInputStream = ObjectInputStream(fileInputStream)
                    try {
                        @Suppress("UNCHECKED_CAST")
                        (objectInputStream.readObject() as List<String>).also { list ->
                            movieTypeList.addAll(list)
                            Log.d(tag, "本地获取到movieType ${movieTypeList.size} 个")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        fileInputStream.close()
                        objectInputStream.close()
                    }
                }
            }
        }
    }
}