package com.swithun.repeatonlifecycle

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    init {
        lifecycle.addObserver(observer = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                Log.d("swithun-xxxx", "[LIFECYEL] $event")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initObserve()
    }

    private fun initObserve() {
        lifecycleScope.launchWhenResumed {
            viewModel.coldFlow().collect {
                Log.d("swithun-xxxx", "[lifecycleScope.launchWhenResumed][cold] $it")
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.hotFlow.collect {
                Log.d("swithun-xxxx", "[lifecycleScope.launchWhenResumed][hot] $it")
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.coldFlow().collect {
                    Log.i("swithun-xxxx", "[repeatOnLifecycle][cold] $it")
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.hotFlow.collect {
                    Log.i("swithun-xxxx", "[repeatOnLifecycle][hot] $it")
                }
            }
        }
    }
}