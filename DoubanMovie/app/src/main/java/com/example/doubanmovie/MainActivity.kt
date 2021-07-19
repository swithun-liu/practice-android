package com.example.doubanmovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.example.doubanmovie.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var client: OkHttpClient
    private lateinit var builder: OkHttpClient.Builder

    private val data = listOf("数据1", "数据2", "数据1", "数据1", "数据1", "数据1", "数据1", "数据1", "数据1", "数据1")

    private val address =
        "https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=20&page_start=0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        builder = OkHttpClient.Builder()
        client = builder.build()

        binding.button.setOnClickListener {
            normalRequest()
        }

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        binding.listView.adapter = adapter

    }

    private fun normalRequest() {

        val request = Request.Builder().url(address).build()

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
                }
            }
        })
    }
}