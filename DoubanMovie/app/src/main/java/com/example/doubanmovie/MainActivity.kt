package com.example.doubanmovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doubanmovie.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val TAG_movie_type = "MainActivity MovieType"
    private lateinit var binding: ActivityMainBinding

    private lateinit var client: OkHttpClient
    private lateinit var builder: OkHttpClient.Builder

    private var episodes = mutableListOf<Episode>()
    private var movieTypes = mutableListOf<String>()

    private val getMovieAddress1 = "https://movie.douban.com/j/search_subjects?type=movie&tag="
    private val getMovieAddress2 = "&sort=recommend&page_limit=20&page_start=0"

    private val getMovieTypeAddress =
        "https://movie.douban.com/j/search_tags?type=movie&tag=%E7%83%AD%E9%97%A8&source="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // ViewBinding
        val view = binding.root
        setContentView(view)

        // OkHttp
        builder = OkHttpClient.Builder()
        client = builder.build()

        // movieItem
        binding.movieItemList.layoutManager = LinearLayoutManager(this)
        val adapter = MovieCardAdapter(episodes)
        binding.movieItemList.adapter = adapter

        // movieType
        binding.movieTypeList.layoutManager =
            LinearLayoutManager(this).also { it.orientation = LinearLayoutManager.HORIZONTAL }
        val movieTypeAdapter = MovieTypeAdapter(movieTypes)
        movieTypeAdapter.setOnItemClickListener(object : MovieTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.d(TAG_movie_type, "点击${movieTypes[position]}")

                // 选中item变色
                movieTypeAdapter.setSelectedPosition(position)
                refreshMovieType(movieTypeAdapter)

                getSubjectsBox(
                    binding.movieItemList.adapter as MovieCardAdapter,
                    movieTypes[position]
                )
            }

            override fun onItemLongClick(view: View, position: Int) {
                Log.d(TAG_movie_type, "长按${movieTypes[position]}")
            }
        })
        binding.movieTypeList.adapter = movieTypeAdapter

        // initialize
        getMovieType(binding.movieTypeList.adapter as MovieTypeAdapter)
        getSubjectsBox(binding.movieItemList.adapter as MovieCardAdapter, null)

    }

    // 刷新 Movie Type
    private fun refreshMovieType(adapter: MovieTypeAdapter) {
        Log.d(TAG_movie_type, "更新电影种类 -- ${adapter.itemCount}")


        adapter.notifyDataSetChanged()
    }

    // 刷新 Movie List
    private fun refreshMovieList(adapter: MovieCardAdapter) {
        Log.d(TAG, "更新List -- ${adapter.itemCount}")
        adapter.notifyDataSetChanged()
    }

    // 获取 Movie Type
    private fun getMovieType(adapter: MovieTypeAdapter) {
        val request = Request.Builder().url(getMovieTypeAddress).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected Code $response")
                    for ((name, value) in response.headers) {
                        Log.d(TAG_movie_type, "MovieType $name: $value")
                    }

                    Log.d(TAG_movie_type, response.toString())

                    var jsonData = response.body?.string() ?: ""
                    Log.d(TAG_movie_type, jsonData)
                    jsonData = jsonData.substringAfter(':')
                    jsonData = jsonData.substringBeforeLast('}')
                    Log.d(TAG_movie_type, jsonData)

                    val gson = GsonBuilder().create()
                    val type = object : TypeToken<List<String>>() {}.type
                    movieTypes.addAll(gson.fromJson(jsonData, type))
                    Log.d(TAG_movie_type, "转换出的movieTypes有 ${movieTypes.size} 个")
                    runOnUiThread { this@MainActivity.refreshMovieType(adapter) }
                }
            }
        })
    }

    // 获取 Movie Item List
    private fun getSubjectsBox(adapter: MovieCardAdapter, movieTag: String?) {

        Log.d(TAG, movieTag.toString())
        val movieTag2 = movieTag ?: "热门"

        val request = Request.Builder().url(getMovieAddress1 + movieTag2 + getMovieAddress2).build()
        Log.d(TAG, getMovieAddress1 + movieTag2 + getMovieAddress2)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected Code $response")
                    for ((name, value) in response.headers) {
                        Log.d(TAG, "$name : $value")
                    }

                    var jsonData = ""

                    Log.d(TAG, response.toString())
                    response.body?.string()?.let { it1 ->
                        Log.d(TAG, it1)
                        jsonData = it1
                    }

                    val gson = GsonBuilder().create()
                    val subjectBox = gson.fromJson(jsonData, SubjectBox::class.java).also {
                        for (i in it.subjects) {
                            Log.d(TAG, i.title)
                        }
                    }
                    episodes.clear()
                    episodes.addAll(subjectBox.subjects)
                    runOnUiThread { this@MainActivity.refreshMovieList(adapter) }
                }
            }
        })
    }
}