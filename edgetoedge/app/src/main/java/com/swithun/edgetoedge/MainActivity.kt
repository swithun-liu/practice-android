package com.swithun.edgetoedge

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    val a = MutableStateFlow(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge(
//            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
//        )
//        swithunEnableEdgeToEdge(
//            navigationBarStyle = SystemBarStyle.auto(
//                this.getColor(R.color.black),
//                this.getColor(R.color.black),
//            )
//        )
        swithunEnableEdgeToEdge(
            navigationBarStyle = com.swithun.edgetoedge.SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
//        transparentNavigationBar(window)
//        transparentNavigationBar(window)
        setContentView(R.layout.activity_main)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        initView()
    }

    private fun initView() {
//        window.decorView.setOnApplyWindowInsetsListener { v, insets ->
//            Log.d("swithun-xxxx", "haha")
//            insets
//        }

        findViewById<TextView>(R.id.tv).setOnClickListener {
            a.update { it + 1 }
        }

        lifecycleScope.launch {
            a.collect { value ->
                findViewById<TextView>(R.id.tv).text = "Hello World $value"
            }
        }
    }
}