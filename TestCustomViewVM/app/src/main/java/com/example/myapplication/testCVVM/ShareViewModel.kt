package com.example.myapplication.testCVVM

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShareViewModel : ViewModel() {
    private val _shareStringFlow = MutableStateFlow<String>("share: string1")
    val shareStringFlow = _shareStringFlow.asStateFlow()

    fun changeText() {
        _shareStringFlow.value = if (_shareStringFlow.value == "share: string1") {
            "share: string2"
        } else {
            "share: string1"
        }
    }
}