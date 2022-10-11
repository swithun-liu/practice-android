package com.example.myapplication.testCVVM

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ParentViewModel: ViewModel() {
    private val _parentStringFlow = MutableStateFlow<String>("share2ParentString: string1")
    val parentStringFlow = _parentStringFlow.asStateFlow()

    fun changeText() {
        _parentStringFlow.value = if (_parentStringFlow.value == "share2ParentString: string1") {
            "share2ParentString: string2"
        } else {
            "share2ParentString: string1"
        }
    }
}