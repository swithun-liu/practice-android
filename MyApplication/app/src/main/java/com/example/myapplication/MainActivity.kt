package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.testAnimation.testHideAndShow.TestHideAndShowAnimationActivity
import com.example.myapplication.testscroll.TestMoveAnimationActivity
import com.example.myapplication.testscroll.TestScrollActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.testScroll.setOnClickListener {
            val intent = Intent(this, TestScrollActivity::class.java)
            startActivity(intent)
        }
        binding.testHideAndShowAnimation.setOnClickListener {
            val intent = Intent(this, TestHideAndShowAnimationActivity::class.java)
            startActivity(intent)
        }
        binding.testMoveAnimation.setOnClickListener {
            val intent = Intent(this, TestMoveAnimationActivity::class.java)
            startActivity(intent)
        }
    }

}