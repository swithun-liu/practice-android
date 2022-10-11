package com.example.myapplication.testCVVM

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.MyViewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * TODO: document your custom view class.
 */
class ChildView : ConstraintLayout {

    private lateinit var b: MyViewBinding
    private var vm: ChildViewModel? = null
    private var shareVm: ShareViewModel? = null
    private var scope: LifecycleCoroutineScope? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        Log.d("swithun-xxxx", "MyView - init")
        b = MyViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun onAttachedToWindow() {
        Log.d("swithun-xxxx", "MyView - onAttachedToWindow")
        super.onAttachedToWindow()
        findViewTreeViewModelStoreOwner()?.let { safeVMSO ->
            vm = ViewModelProvider(safeVMSO)[ChildViewModel::class.java]
            shareVm = ViewModelProvider(safeVMSO)[ShareViewModel::class.java]
        }
        scope = findViewTreeLifecycleOwner()?.lifecycleScope

        initClick()
        initObserve()
    }

    private fun initClick() {
        b.childStringBtn.setOnClickListener {
            vm?.changeText()
        }
        b.shareStringChildBtn.setOnClickListener {
            shareVm?.changeText()
        }
    }

    private fun initObserve() {
        Log.d("swithun-xxxx", "MyView - initObserve")
        scope?.launch {
            vm?.childStringFlow?.collect {
                b.childString.text = it
            }
        }
        scope?.launch {
            shareVm?.shareStringFlow?.collect {
                b.shareStringChild.text = it
            }
        }
    }

}