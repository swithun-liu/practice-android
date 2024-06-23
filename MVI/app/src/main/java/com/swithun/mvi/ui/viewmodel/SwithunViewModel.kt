package com.swithun.mvi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swithun.mvi.domain.ChangeData12UseCase
import com.swithun.mvi.domain.ChangeData1UseCase
import com.swithun.mvi.domain.ChangeData2UseCase
import com.swithun.mvi.data.Data1Repository
import com.swithun.mvi.data.Data2Repository
import com.swithun.mvi.ui.contract.UIAction
import com.swithun.mvi.ui.contract.UIEvent
import com.swithun.mvi.ui.contract.UIState
import com.swithun.mvi.ui.contract.asNormal
import com.swithun.mvi.ui.contract.toLogStr
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SwithunViewModel : ViewModel() {

    private val _state: MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)
    val state: StateFlow<UIState> = _state.asStateFlow()

    private val _event: MutableSharedFlow<UIEvent> = MutableSharedFlow()
    val event: SharedFlow<UIEvent> = _event.asSharedFlow()

    private val data1Repository = Data1Repository()
    private val data2Repository = Data2Repository()

    fun init(initData: InitData) {
        Log.d(TAG, "[init] ${initData.data}")
        initObserve()
        _state.value =
            UIState.Normal("我是Data1[${data1Repository.getData1()}]", data2Repository.data2.value)
    }

    fun reduce(action: UIAction) {
        Log.d(TAG, "[reduce] ${action.toLogStr()}")
        when (action) {
            is UIAction.ClickAAAction -> handleClickAAAction()
            is UIAction.ClickABAction -> handleClickABAction()
            is UIAction.ClickBAction -> handleClickBAction()
            is UIAction.ClosePageAction -> { }
        }
    }

    private fun handleClickAAAction() {
        viewModelScope.launch {
            ChangeData1UseCase(data1Repository).execute().collect { newData1 ->
                setNormaState { copy(data1 = "我是Data1[${newData1}]") }
                _event.emit(UIEvent.ToastEvent("我是Data1[${newData1}]"))
            }
        }
    }

    private fun handleClickABAction() {
        viewModelScope.launch {
            ChangeData12UseCase(data1Repository, data2Repository).execute()
                .collect { (type, value) ->
                    when (type) {
                        "Data1" -> {
                            setNormaState { copy(data1 = "我是Data1[${value}]") }
                            _event.emit(UIEvent.ToastEvent("我是Data1[${value}]"))
                        }

                        else -> {}
                    }
                }
        }
    }

    private fun handleClickBAction() {
        viewModelScope.launch {
            ChangeData2UseCase(data2Repository).execute()
            _event.emit(UIEvent.ToastEvent("我是Data2[${data2Repository.data2.value}]"))
        }
    }

    private fun initObserve() {
        viewModelScope.launch {
            data2Repository.data2.collect { newData2 ->
                setNormaState { copy(data2 = newData2) }
            }
        }
    }

    private fun setNormaState(reducer: UIState.Normal.() -> UIState.Normal) {
        this._state.value.asNormal { normal ->
            this._state.value = reducer(normal)
        }
    }

    data class InitData(val data: String)

    data class VMData(val flag1: Int, val otherData: String)

    companion object {
        private const val TAG = "SwithunViewModel"
    }
}