package com.example.learn_coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learn_coroutine.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /**
         * getUser1 { user1 ->
         *     getUser2 { user2 ->
         *         getMessages { message ->
         *             // construct our chat object
         *         }
         *     }
         * }
         */

    }
}