package com.swithun.mvi.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.swithun.mvi.databinding.ActivityMainBinding
import com.swithun.mvi.ui.contract.UIAction
import com.swithun.mvi.ui.contract.toLogStr
import com.swithun.mvi.ui.view.SwithunView
import com.swithun.mvi.ui.viewmodel.SwithunViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var view: SwithunView? = null
    private val viewModel: SwithunViewModel by viewModels<SwithunViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initViewModel()
        initView()
        initObserve()
    }

    private fun initViewModel() {
        viewModel.init(SwithunViewModel.InitData("I am init data"))
    }

    private fun initView() {
        ActivityMainBinding.inflate(LayoutInflater.from(this)).let { binding ->
            setContentView(binding.root)
            this.view = SwithunView(binding, ::reduce)
        }
    }

    private fun initObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    view?.bindState(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.event.collect {
                    view?.handleEvent(it)
                }
            }
        }
    }

    private fun reduce(action: UIAction) {
        Log.d(TAG, "[reduce] ${action.toLogStr()}")
        when (action) {
            is UIAction.ClosePageAction -> { }
            else -> viewModel.reduce(action)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}