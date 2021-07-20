package com.example.doubanmovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import coil.load
import com.example.doubanmovie.databinding.ActivityMovieDetailBinding
import com.example.doubanmovie.entity.MovieDetail
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MovieDetailActivity : AppCompatActivity() {

    private val TAG = "MovieDetailActivity"
    private lateinit var binding: ActivityMovieDetailBinding

    private lateinit var builder: OkHttpClient.Builder
    private lateinit var client: OkHttpClient

    private var movieDetail: MovieDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        builder = OkHttpClient.Builder()
        client = builder.build()

        val url = intent.getStringExtra("url").toString()
        Log.d(TAG, url)
        getMovieDetail(url)
    }

    private fun refreshUI() {
        movieDetail?.let {
            binding.imageView.load(it.image)
            binding.movieDetailName.text = it.name
            binding.moiveDetailPublishDate.text = it.datePublished
        }
    }

    private fun getMovieDetail(url: String) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.d(TAG, "失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected Code $response")

                    for ((name, value) in response.headers) {
                        Log.d(TAG, "$name: $value")
                    }
                    response.body?.let { it1 ->
                        Log.d(TAG, it1.toString())
                        var urlAfterSub =
                            it1.string().substringAfter("<script type=\"application/ld+json\">")
                        urlAfterSub = urlAfterSub.substringBefore("</script>")
                        Log.d(TAG, urlAfterSub)

                        val gson = GsonBuilder().create()
                        movieDetail = gson.fromJson(urlAfterSub, MovieDetail::class.java)

                        Log.d(TAG, movieDetail!!.url)

                        runOnUiThread {
                            this@MovieDetailActivity.refreshUI()
                        }
                    }
                }
            }

        })
    }
}