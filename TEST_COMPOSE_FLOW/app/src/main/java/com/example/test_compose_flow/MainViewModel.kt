package com.example.test_compose_flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    val countDownFlow = flow<Int> {
        val startingValue = 5
        var currentValue = startingValue
        emit(startingValue)
        while (currentValue > 0) {
            delay(1000L)
            currentValue--
            emit(currentValue)
        }
    }

    private val _stateFlow = MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()

    init {
        collectFlow()
    }

    fun incrementCounter() {
        _stateFlow.value += 1
    }

    private fun collectFlow() {
        val flow = flow {
            delay(250L)
            emit("Appetizer")
            delay(1000L)
            emit("Main dish")
            delay(100L)
            emit("Dessert")
        }
        viewModelScope.launch {
            flow.onEach {
                println("swithun-Flow: $it is delivered")
            }
                .conflate()
                .collect {
                    println("swithun-Flow: Now eating $it")
                    delay(1500L)
                    println("swithun-Flow: Finished eating $it")
                }
        }
    }
}