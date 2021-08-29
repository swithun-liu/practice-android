package com.example.myapplication.testscroll

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityTestMoveAnimationBinding

class TestMoveAnimationActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestMoveAnimationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTestMoveAnimationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.testTranslationBtn.setOnClickListener {
            ObjectAnimator.ofFloat(binding.testTranslationBtn, "translationX", 30f).apply {
                duration = 2000
                start()
            }
        }

    }
}