package com.example.myapplication.testCVVM

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class ChildBaseViewMode: ViewModel() {

    private val _childStringFlow = MutableStateFlow<String>("child1: string1")
    open val childStringFlow = _childStringFlow.asStateFlow()

    open fun changeText() {
        _childStringFlow.value = if (_childStringFlow.value == "child1: string1") {
            "child1: string2"
        } else {
            "child1: string1"
        }
    }

}

class ChildViewModel1 : ChildBaseViewMode() {
    private val _childStringFlow = MutableStateFlow<String>("child1: string1")
    override val childStringFlow = _childStringFlow.asStateFlow()

    override fun changeText() {
        _childStringFlow.value = if (_childStringFlow.value == "child1: string1") {
            "child1: string2"
        } else {
            "child1: string1"
        }
    }

}

class ChildViewModel2: ChildBaseViewMode() {
    private val _childStringFlow = MutableStateFlow<String>("child2: string1")
    override val childStringFlow = _childStringFlow.asStateFlow()

    override fun changeText() {
        _childStringFlow.value = if (_childStringFlow.value == "child2: string1") {
            "child2: string2"
        } else {
            "child2: string1"
        }
    }
}