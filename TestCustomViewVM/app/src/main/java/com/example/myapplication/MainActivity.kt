package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.testCVVM.ParentViewModel
import com.example.myapplication.testCVVM.ShareViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var b: ActivityMainBinding
    private val vm: ParentViewModel by lazy {
        ViewModelProvider(this)[ParentViewModel::class.java]
    }
    private val shareVM: ShareViewModel by lazy {
        ViewModelProvider(this)[ShareViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("swithun-xxxx", "MainActivity  onCreate")
        b = ActivityMainBinding.inflate(LayoutInflater.from(this))
        super.onCreate(savedInstanceState)
        setContentView(b.root)
        Log.d("swithun-xxxx", "MainActivity after setcontentview")
        initClick()
        initObserve()
    }

    private fun initClick() {
        b.parentStringBtn.setOnClickListener {
            vm.changeText()
        }
        b.shareStringParentBtn.setOnClickListener {
            shareVM.changeText()
        }
    }

    private fun initObserve() {
        Log.d("swithun-xxxx", "MainActivity  initObserve")
        lifecycleScope.launch {
            vm.parentStringFlow.collect {
                b.parentString.text = it
            }
        }
        lifecycleScope.launch {
            shareVM.shareStringFlow.collect {
                b.shareStringParent.text = it
            }
        }
    }
}