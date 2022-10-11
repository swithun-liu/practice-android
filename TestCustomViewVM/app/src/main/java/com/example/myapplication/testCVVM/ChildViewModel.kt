package com.example.myapplication.testCVVM

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChildViewModel : ViewModel() {
    private val _childStringFlow = MutableStateFlow<String>("child: string1")
    val childStringFlow = _childStringFlow.asStateFlow()

    fun changeText() {
        _childStringFlow.value = if (_childStringFlow.value == "child: string1") {
            "child: string2"
        } else {
            "child: string1"
        }
    }

}