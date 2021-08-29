package com.example.myapplication.testAnimation.testHideAndShow

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityTestHideAndShowAnimationBinding
import kotlin.properties.Delegates

class TestHideAndShowAnimationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestHideAndShowAnimationBinding
    private var shortAnimationDuration = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTestHideAndShowAnimationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        binding.button1.setOnClickListener {
            crossFade1()
        }
        binding.button2.setOnClickListener {
            crossFade2()
        }
    }

    private fun crossFade1() {
        binding.button2.apply {
            alpha = 0f
            visibility = View.VISIBLE
        }
        binding.button2.animate().alpha(1f).setDuration(shortAnimationDuration.toLong())
            .setListener(null)
        binding.button1.animate().alpha(0f).setDuration(shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.button1.visibility = View.GONE
                }
            })
    }

    private fun crossFade2() {
        binding.button1.apply {
            alpha = 0f
            visibility = View.VISIBLE
        }
        binding.button1.animate().alpha(1f).setDuration(shortAnimationDuration.toLong())
            .setListener(null)
        binding.button2.animate().alpha(0f).setDuration(shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.button2.visibility = View.GONE
                }
            })
    }
}
