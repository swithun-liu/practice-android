package com.example.test_compose_flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    // normal flow
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

    init {
        // collectFlow()
    }

    // stateFlow
    private val _stateFlow = MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()

    fun incrementCounter() {
        _stateFlow.value += 1
    }

    // sharedFlow
    private val _sharedFlow = MutableSharedFlow<Int>(replay = 100)
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun squareNumber(number: Int) {
        viewModelScope.launch {
            _sharedFlow.emit(number * number)
        }
    }

    init {
        viewModelScope.launch {
            // collector 1
            _sharedFlow.collect {
                delay(2000L)
                println("swithun-xxxxFirst  Flow: The received number is $it")
            }
            println("swithun-xxxxFirst  CoroutineScope continue")
        }

        viewModelScope.launch {
            // collector 2
            _sharedFlow.collect {
                delay(3000L)
                println("swithun-xxxxSecond Flow: The received number is $it")
            }
            println("swithun-xxxxSecond CoroutineScope continue")
        }

        squareNumber(3) // must be called after collectors is set if you didn't set replay - MutableSharedFlow<Int>(""""replay"""" = 100)
    }

}