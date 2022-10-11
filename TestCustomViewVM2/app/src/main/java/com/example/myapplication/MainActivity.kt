package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.testCVVM.ChildViewDataObserver
import com.example.myapplication.testCVVM.ChildViewModel1
import com.example.myapplication.testCVVM.ChildViewModel2
import com.example.myapplication.testCVVM.ParentViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var b: ActivityMainBinding
    private val vm: ParentViewModel by lazy {
        ViewModelProvider(this)[ParentViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("swithun-xxxx", "MainActivity  onCreate")
        b = ActivityMainBinding.inflate(LayoutInflater.from(this))
        super.onCreate(savedInstanceState)
        setContentView(b.root)
        Log.d("swithun-xxxx", "MainActivity after setcontentview")

        initView()
        initClick()
        initObserve()
    }

    private fun initView() {
        b.myView.setDependency(ChildViewModel1::class.java, "initialString", object : ChildViewDataObserver {
            override fun onShareStringChanged(shareText: String) {
                b.shareStringParent.text = shareText
            }
        })

        b.myView.setDependency(ChildViewModel2::class.java, 1010101, object : ChildViewDataObserver {
            override fun onShareStringChanged(shareText: String) {
                b.shareStringParent.text = shareText
            }
        })
    }

    private fun initClick() {
        b.parentStringBtn.setOnClickListener {
            vm.changeText()
        }
    }

    private fun initObserve() {
        Log.d("swithun-xxxx", "MainActivity  initObserve")
        lifecycleScope.launch {
            vm.parentStringFlow.collect {
                b.parentString.text = it
            }
        }

    }

}
