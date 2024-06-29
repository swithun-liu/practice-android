package com.swithun.repeatonlifecycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    fun coldFlow() = flow<Int> {
        var a = 1
        while (true) {
            delay(1000)
            emit(a)
            a++
        }
    }

    var hotFlow: MutableStateFlow<Int> = MutableStateFlow(0)

    init {
        var a = 1
        viewModelScope.launch {
            while (true) {
                delay(1000)
                hotFlow.emit(a)
                a++
            }
        }
    }

}