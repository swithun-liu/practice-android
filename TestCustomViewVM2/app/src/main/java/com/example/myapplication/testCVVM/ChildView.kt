package com.example.myapplication.testCVVM

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.MyViewBinding
import kotlinx.coroutines.launch

/**
 * TODO: document your custom view class.
 */

class ChildView<ChildViewModel: ChildBaseViewMode<InitialData>, InitialData> : ConstraintLayout {


    private lateinit var b: MyViewBinding
    private var vm: ChildBaseViewMode<InitialData>? = null
    private var scope: LifecycleCoroutineScope? = null

    private var vmClass: Class<ChildViewModel>? = null
    private var initialData : InitialData? = null
    private var outerObserver: ChildViewDataObserver? = null


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        Log.d("swithun-xxxx", "MyView - init")
        b = MyViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setDependency(java: Class<ChildViewModel>, initialData: InitialData, outerObserver: ChildViewDataObserver) {
        Log.d("swithun-xxxx", "[setDependency] : $initialData")
        this.vmClass = java
        this.initialData = initialData
        this.outerObserver = outerObserver
    }

    override fun onAttachedToWindow() {
        Log.d("swithun-xxxx", "MyView - onAttachedToWindow")
        super.onAttachedToWindow()

        initialize()
        initView()
        initClick()
        initObserve()
    }

    private fun initialize() {
        vm = vmClass?.let { safeVmClass ->
            findViewTreeViewModelStoreOwner()?.let { safeVMSO ->
                ViewModelProvider(safeVMSO)[safeVmClass]
            }
        }
        scope = findViewTreeLifecycleOwner()?.lifecycleScope
    }

    private fun initView() {
        initialData?.let { vm?.initVM(it) }
    }

    private fun initClick() {
        b.childStringBtn.setOnClickListener {
            vm?.changeText()
        }
        b.shareStringChildBtn.setOnClickListener {
            vm?.changeShareText()
        }
        R.styleable.ChildView_viewModel
    }

    private fun initObserve() {
        Log.d("swithun-xxxx", "MyView - initObserve")
        scope?.launch {
            vm?.childStringFlow?.collect {
                b.childString.text = it
            }
        }
        scope?.launch {
            vm?.shareStringFLow?.collect {
                b.shareStringChild.text = it
                outerObserver?.onShareStringChanged(it)
            }
        }
    }

}

inline fun <reified T : Enum<T>> TypedArray.getEnum(index: Int, default: T) =
    getInt(index, -1).let {
        if (it >= 0) enumValues<T>()[it] else default
    }


interface ChildViewDataObserver  {
    fun onShareStringChanged(shareText: String)
}
