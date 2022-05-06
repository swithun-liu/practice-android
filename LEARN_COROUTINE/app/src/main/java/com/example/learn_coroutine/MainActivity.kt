package com.example.learn_coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.learn_coroutine.databinding.ActivityMainBinding
import com.google.android.gms.tasks.SuccessContinuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.LoadBundleTask
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import swithunLog

const val BASE_URL = "https://jsonplaceholder.typicode.com/"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyAPI::class.java)

        /* old version */
        api.getComments().enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (comment in it) {
                            swithunLog(comment.toString())
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                swithunLog("Error: $t")
            }

        })

        /* coroutine version */
        GlobalScope.launch(Dispatchers.IO) {
            // without check version
            val comments = api.getComments().await()
            for (comment in comments) {
                swithunLog(comment.toString())
            }
            // with check version
            val response = api.getComments().awaitResponse()
            if (response.isSuccessful) {
                response.body()?.let { comments ->
                    for (comment in comments) {
                        swithunLog(comment.toString())
                    }
                }
            }
        }

        /* better coroutine version */
        GlobalScope.launch(Dispatchers.IO) {
            val response = api.betterGetComments()
            if (response.isSuccessful) {
                response.body()?.let { comments ->
                    for (comment in comments) {
                        swithunLog(comment.toString())
                    }
                }
            }
        }

    }
}