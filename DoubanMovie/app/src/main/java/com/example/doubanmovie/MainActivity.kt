package com.example.doubanmovie

import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doubanmovie.databinding.ActivityMainBinding
import com.example.doubanmovie.entity.Episode
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

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val TAG_movie_type = "MainActivity MovieType"
    private lateinit var binding: ActivityMainBinding

    private lateinit var client: OkHttpClient
    private lateinit var builder: OkHttpClient.Builder

    private var episodes = mutableListOf<Episode>()
    private var movieTypes = mutableListOf<String>()
    private var currentMovieTag = "热门"

    private val getMovieAddress1 = "https://movie.douban.com/j/search_subjects?type=movie&tag="
    private val getMovieAddress2 = "&sort=recommend&page_limit=20&page_start="

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
        val movieItemLinearLayoutManager = LinearLayoutManager(this)
        binding.movieItemList.layoutManager = movieItemLinearLayoutManager
        val movieCardAdapter = MovieCardAdapter(episodes)
        movieCardAdapter.setOnItemClickListener(object : MovieCardAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.d(TAG, "点击图片")
                val intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
                intent.putExtra("url", episodes[position].url)
                startActivity(intent)
            }

            override fun onItemLongClick(view: View, position: Int) {
                Log.d(TAG, "长按图片")
            }

        })
        binding.movieItemList.adapter = movieCardAdapter

        // movieItem 加载更多
        binding.movieItemList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (movieItemLinearLayoutManager.findLastCompletelyVisibleItemPosition() == episodes.size - 1) {
                    Log.d(TAG, "得加载了")
                    getMovieItems(movieCardAdapter, currentMovieTag, episodes.size)
                }
            }
        })

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
                currentMovieTag = movieTypes[position]
                episodes.clear()

                getMovieItems(
                    binding.movieItemList.adapter as MovieCardAdapter, movieTypes[position], null
                )
            }

            override fun onItemLongClick(view: View, position: Int) {
                Log.d(TAG_movie_type, "长按${movieTypes[position]}")
            }
        })
        binding.movieTypeList.adapter = movieTypeAdapter

        // initialize form device

        getMovieData(this, currentMovieTag)
        refreshMovieList(movieCardAdapter)

        // initialize from Internet
        getMovieType(binding.movieTypeList.adapter as MovieTypeAdapter)
        getMovieItems(binding.movieItemList.adapter as MovieCardAdapter, null, null)

    }

    // 保存电影种类数据
    private fun setMovieTypeData(context: Context, list: List<String>) {
        Log.d(TAG, "保存电影种类数据")
    }

    // 保存电影列表数据
    private fun setMovieData(context: Context, episodes: MutableList<Episode>, movieTag: String) {
        Log.d(TAG, "保存数据")
        val cacheParent = File(context.cacheDir.path.toString() + "/movieItem").also {
            if (!it.exists()) it.mkdir()
        }
        val cache = File(cacheParent, "$movieTag")
        val fileOutputStream = FileOutputStream(cache)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)
        objectOutputStream.writeObject(episodes)
        fileOutputStream.close()
        objectOutputStream.close()
    }

    // 读取列表数据
    private fun getMovieData(context: Context, movieTag: String) {
        Log.d(TAG, "获取数据 -- movie_$movieTag")
        val cache = File("${context.cacheDir.path.toString()}/movieItem/$movieTag").let {
            if (it.exists()) {
                val fileInputStream = FileInputStream(it)
                val objectInputStream = ObjectInputStream(fileInputStream)
                episodes.addAll(objectInputStream.readObject() as List<Episode>)
            }
        }
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
                    runOnUiThread {
                        refreshMovieType(adapter)
                    }
                }
            }
        })
    }

    // 获取 Movie Item List
    private fun getMovieItems(adapter: MovieCardAdapter, movieTag: String?, pageStart: Int?) {

        Log.d(TAG, "############## getMovieItems ###########")
        Log.d(TAG, "movieTag: $movieTag --- pageStart: $pageStart")
        Log.d(TAG, movieTag.toString())
        val movieTag2 = movieTag ?: "热门"
        var pageStartString = "0"
        pageStart?.let {
            pageStartString = pageStart.toString()
        }

        val request =
            Request.Builder().url(getMovieAddress1 + movieTag2 + getMovieAddress2 + pageStartString)
                .build()
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
                    episodes.addAll(subjectBox.subjects)
                    setMovieData(this@MainActivity, episodes, currentMovieTag)
                    runOnUiThread {
                        refreshMovieList(adapter) //                        setData(this@MainActivity, episodes, currentMovieTag)
                    }
                }
            }
        })
    }
}