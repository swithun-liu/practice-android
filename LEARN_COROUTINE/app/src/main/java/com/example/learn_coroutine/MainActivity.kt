package com.example.learn_coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import swithunPrint

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runBlocking {
            launch(Dispatchers.IO) {
                delay(3000L)
                swithunPrint("Finish IO coroutine 1")
            }

            launch(Dispatchers.IO) {
                delay(3000L)
                swithunPrint("Finish IO coroutine 1")
            }
        }

        GlobalScope.launch {
            launch(Dispatchers.IO) {
                delay(3000L)
                swithunPrint("Finish GS IO coroutine 1")
            }

            launch(Dispatchers.IO) {
                delay(3000L)
                swithunPrint("Finish GS IO coroutine 1")
            }
        }

    }
}