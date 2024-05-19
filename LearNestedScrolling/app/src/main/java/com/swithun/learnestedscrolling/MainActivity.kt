package com.swithun.learnestedscrolling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.swithun.learnestedscrolling.databinding.ActivityMain2Binding
import com.swithun.learnestedscrolling.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

//    private var binding: ActivityMainBinding? = null
    private var binding: ActivityMain2Binding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(LayoutInflater.from(this)).also {
//            setContentView(it.root)
//            it.parent.TAG = "parent"
//            it.child.TAG = "child"
//        }
        binding = ActivityMain2Binding.inflate(LayoutInflater.from(this)).also {
            setContentView(it.root)
        }
    }
}