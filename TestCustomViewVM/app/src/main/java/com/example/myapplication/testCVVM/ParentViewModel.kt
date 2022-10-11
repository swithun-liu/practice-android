package com.example.myapplication.testCVVM

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ParentViewModel: ViewModel() {
    private val _parentStringFlow = MutableStateFlow<String>("parent: string1")
    val parentStringFlow = _parentStringFlow.asStateFlow()

    fun changeText() {
        _parentStringFlow.value = if (_parentStringFlow.value == "parent: string1") {
            "parent: string2"
        } else {
            "parent: string1"
        }
    }
}