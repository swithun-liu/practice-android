package com.example.test_compose_flow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test_compose_flow.ui.theme.TEST_COMPOSE_FLOWTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    /* stateFlow version 2-2:2' xml */
    suspend fun test(number: Int) {
        // mock xml viewbinding
        // binding.tvCounter.text = number.toString
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        /* stateFlow version2-1 xml */
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collectLatest { number ->
                    // mock xml viewbinding
                    // binding.tvCounter.text = number.toString
                }
            }
        }

        /* stateFlow version 2-2:2 xml */
        collectLatestLifecycleFlow(viewModel.stateFlow) { number ->
            // mock xml viewbinding
            // binding.tvCounter.text = number.toString
        }
        /* stateFlow version 2-2:2' xml */
        collectLatestLifecycleFlow(viewModel.stateFlow, this::test)

        super.onCreate(savedInstanceState)
        /* stateFlow version 1 component*/
        setContent {
            TEST_COMPOSE_FLOWTheme {
                val viewModel = viewModel<MainViewModel>()
                // stateFlow
                val count = viewModel.stateFlow.collectAsState(initial = 0)

                // sharedFlow
                LaunchedEffect(key1 = true) {
                    viewModel.sharedFlow.collect { number ->
                        println("swithun-xxxx: MainActivity: $number")
                    }
                }

                // A surface container using the 'background' color from the theme
                Box(modifier = Modifier.fillMaxSize()) {
                    Button(onClick = { viewModel.incrementCounter() }) {
                        Text(text = "Counter: ${count.value}")
                    }
                }
            }
        }
    }
}

/* stateFlow version 2-2:1 xml */
fun <T> ComponentActivity.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

/* sharedFlow version 2 xml */
fun <T> ComponentActivity.collectLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}
