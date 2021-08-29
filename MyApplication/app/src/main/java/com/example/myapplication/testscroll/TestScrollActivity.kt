package com.example.myapplication.testscroll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityTestScrollBinding

class TestScrollActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestScrollBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTestScrollBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.testScrollByBtn.setOnClickListener {
            testScroll()
        }
    }

    private fun testScroll() {
        Toast.makeText(this, "scrollBy - 30 - 30", Toast.LENGTH_SHORT).show()
        binding.testScrolllayout.scrollBy(30, -30)
    }
}