package com.swithun.myapplication

import android.os.Bundle
import com.swithun.myapplication.databinding.ActivityThirdBinding

class ThirdActivity : BaseActivity() {
    private lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.button3.setOnClickListener {
            ActivityCollector.finishAll()
        }
    }
}