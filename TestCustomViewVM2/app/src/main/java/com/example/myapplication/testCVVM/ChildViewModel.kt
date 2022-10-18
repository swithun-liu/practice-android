package com.example.myapplication.testCVVM

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class ChildBaseViewMode<InitialData> : ViewModel() {

    abstract val childStringFlow: StateFlow<String>

    abstract val shareStringFLow: StateFlow<String>

    abstract fun initVM(initialData: InitialData)

    abstract fun changeText()

    abstract fun changeShareText()

}

class ChildViewModel1 : ChildBaseViewMode<String>() {
    private val _childStringFlow = MutableStateFlow("child1: string1")
    override val childStringFlow= _childStringFlow.asStateFlow()

    private val _shareStringFlow = MutableStateFlow<String>("share: string1")
    override val shareStringFLow = _shareStringFlow .asStateFlow()

    override fun initVM(initialData: String) {
        Log.d("swithun-xxxx", "initial by string: $initialData")
        _childStringFlow.value = initialData
    }

    override fun changeText() {
        _childStringFlow.value = if (_childStringFlow.value == "child1: string1") {
            "child1: string2"
        } else {
            "child1: string1"
        }
    }

    override fun changeShareText() {
        _shareStringFlow.value = if (_shareStringFlow.value == "share: string1") {
            "share: string2"
        } else {
            "share: string1"
        }
    }

}

class ChildViewModel2: ChildBaseViewMode<Int>() {

    private val _childStringFlow = MutableStateFlow("child1: string1")
    override val childStringFlow = _childStringFlow.asStateFlow()

    private val _shareStringFlow = MutableStateFlow("share: string1")
    override val shareStringFLow = _shareStringFlow .asStateFlow()

    override fun initVM(initialData: Int) {
        Log.d("swithun-xxxx", "initial by Int: $initialData")
        _childStringFlow.value = initialData.toString()
    }

    override fun changeText() {
        _childStringFlow.value = if (_childStringFlow.value == "child2: string1") {
            "child2: string2"
        } else {
            "child2: string1"
        }
    }

    override fun changeShareText() {
        _shareStringFlow.value = if (_shareStringFlow.value == "share: string1") {
            "share: string2"
        } else {
            "share: string1"
        }
    }
}


/**
 * 需要给ChildView设置的初始化数据
 * @param vmClass ChildViewModel的class
 * @param initialData ViewModel基于此数据初始化，类型基于ChildViewModel子类声明的范型 InitialData
 * @param notifier 当数据变化时ChildView可以给外部提供的通知
 */
class ChildViewDependency<ChildViewModel : ChildBaseViewMode<InitialData>, InitialData>(
    val vmClass: Class<ChildViewModel>,
    val initialData: InitialData,
    val notifier: ChildViewDataChangeNotifier
)