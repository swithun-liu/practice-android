package com.swithun.mvi.ui.view

import android.util.Log
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.swithun.mvi.ui.contract.UIAction
import com.swithun.mvi.ui.contract.UIState
import com.swithun.mvi.databinding.ActivityMainBinding
import com.swithun.mvi.ui.contract.UIEvent
import com.swithun.mvi.ui.contract.toLogStr
import kotlin.time.Duration

class SwithunView(
    private val binding: ActivityMainBinding,
    private val reduce: (UIAction) -> Unit
) {

    private val context = binding.root.context

    init {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.AA.setOnClickListener { reduce(UIAction.ClickAAAction("AA Info")) }
        binding.AB.setOnClickListener { reduce(UIAction.ClickABAction("AB Info")) }
        binding.B.setOnClickListener { reduce(UIAction.ClickBAction("B Info")) }
    }

    fun bindState(state: UIState) {
        Log.d(TAG, "[bindState] ${state.toLogStr()}")
        when (state) {
            UIState.Loading -> showLoading()
            is UIState.Normal -> showNormal(state)
        }
    }

    fun handleEvent(event: UIEvent) {
        Log.d(TAG, "[handleEvent] ${event.toLogStr()}")
        when (event) {
            is UIEvent.ToastEvent -> {
                Toast.makeText(context, "Event: ${event.msg}", Toast.LENGTH_SHORT).show()
            }
            UIEvent.ClosePageEvent -> { }
        }
    }

    private fun showLoading() {

    }

    private fun showNormal(state: UIState.Normal) {
        bindA(state.data1, state.data2)
        bindB(state.data2)
    }

    private fun bindA(data1: String, data2: Int?) {
        binding.AA.text = "A_A: [data1($data1)]"
        binding.AB.text = "A_B: [data1($data1), data2($data2)]"
    }

    private fun bindB(data2: Int?) {
        binding.B.text = "B: [data2($data2)]"
    }


    companion object {
        private const val TAG = "SwithunView"
    }

}