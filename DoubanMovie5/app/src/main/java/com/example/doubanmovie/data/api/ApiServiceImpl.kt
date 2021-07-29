package com.example.doubanmovie.data.api

import android.content.Context
import android.util.Log
import com.example.doubanmovie.data.model.Episode
import com.example.doubanmovie.data.model.SubjectBox
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

class ApiServiceImpl : ApiService {

    private var client: OkHttpClient
    private var builder: OkHttpClient.Builder = OkHttpClient.Builder()
    private val getMovieTypeAddress =
        "https://movie.douban.com/j/search_tags?type=movie&tag=%E7%83%AD%E9%97%A8&source="
    private val tag = "MainActivity"
    private val getMovieAddress1 = "https://movie.douban.com/j/search_subjects?type=movie&tag="
    private val getMovieAddress2 = "&sort=recommend&page_limit=20&page_start="

    init {
        client = builder.build()
    }

    override suspend fun getEpisodes(): retrofit2.Call<List<Episode>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieTypesFromInternet(): List<String> {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder().url(getMovieTypeAddress).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Unexpected Code $response")
            for ((name, value) in response.headers) {
                Log.d(tag, "MovieType $name: $value")
            }

            Log.d(tag, response.toString())

            var jsonData = response.body?.string() ?: ""
            Log.d(tag, jsonData)
            jsonData = jsonData.substringAfter(':')
            jsonData = jsonData.substringBeforeLast('}')
            Log.d(tag, jsonData)

            val gson = GsonBuilder().create()
            val type = object : TypeToken<List<String>>() {}.type
            val movieTypeList = mutableListOf<String>()
            movieTypeList.addAll(gson.fromJson(jsonData, type))
            Log.d(tag, "转换出的movieTypes有 ${movieTypeList.size} 个")
            movieTypeList
        }
    }

    override suspend fun getMovieItemsFromInternet(
        movieTag: String?, pageStart: Int?
    ): List<Episode> {

        return withContext(Dispatchers.IO) {
            Log.d(tag, "############## getMovieItems ###########")

            Log.d(tag, "movieTag: $movieTag --- pageStart: $pageStart")

            val movieTag2 = movieTag ?: "热门"
            var pageStartString = pageStart ?: "0"
            val request =
                Request.Builder().url(getMovieAddress1 + movieTag2 + getMovieAddress2 + pageStartString)
                    .build().also { Log.d(tag, it.url.toString()) }
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Unexpected Code $response")
            var jsonData = (response.body?.string() ?: "").also { Log.d(tag, "jsonData = $it") }
            val gson = GsonBuilder().create()
            val subjectBox = gson.fromJson(jsonData, SubjectBox::class.java).also {
                Log.d(tag, "subjects: ${it.subjects}")
            }

            subjectBox.subjects
        }
    }

    // 本地：保存电影列表
    override suspend fun setMovieItemToFile(context: Context, movieTag: String, episodeList: MutableList<Episode>) {
        withContext(Dispatchers.IO) {
            Log.d(tag, "保存到本地 -- $movieTag -- ${episodeList.size}条 2")
            val tempEpisodeList: MutableList<Episode> = mutableListOf()
            tempEpisodeList.addAll(episodeList)
            val cacheParent = File(context.cacheDir.path + "/movieItem").also {
                if (!it.exists()) it.mkdir()
            }
            val cache = File(cacheParent, movieTag)
            val fileOutputStream = FileOutputStream(cache)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(tempEpisodeList)
            episodeList.addAll(tempEpisodeList)
            fileOutputStream.close()
            objectOutputStream.close()
        }
    }

    // 本地：获取电影列表
    override suspend fun getMovieItemFromFile(context: Context, movieTag: String): MutableList<Episode> {
        return withContext(Dispatchers.IO) {
            Log.d(tag, "本地获取数据 -- movie_$movieTag")
            File("${context.cacheDir.path}/movieItem/$movieTag").let {
                if (it.exists()) {
                    val fileInputStream = FileInputStream(it)
                    val objectInputStream = ObjectInputStream(fileInputStream)
                    try {
                        (objectInputStream.readObject() as List<Episode>).also { episode ->
                            fileInputStream.close()
                            objectInputStream.close()
                            Log.d(tag, "本地获取数据 -- movie_$movieTag -- ${episode.size}个")
                        } as MutableList<Episode>
                    } catch (e: IOException) {
                        e.printStackTrace()
                        mutableListOf<Episode>()
                    } finally {
                        fileInputStream.close()
                        objectInputStream.close()
                    }
                } else {
                    mutableListOf()
                }
            }
        }
    }
}
