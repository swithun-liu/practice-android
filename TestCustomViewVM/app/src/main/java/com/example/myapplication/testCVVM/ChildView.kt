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
class ChildView : ConstraintLayout {

    private lateinit var b: MyViewBinding
    private var vm: ChildBaseViewMode? = null
    private var shareVm: ShareViewModel? = null
    private var scope: LifecycleCoroutineScope? = null
    private var vmType: VMType = VMType.CHILD_VM1


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
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ChildView, defStyle, 0
        )
        vmType = a.getEnum(R.styleable.ChildView_viewModel, VMType.CHILD_VM1)

        b = MyViewBinding.inflate(LayoutInflater.from(context), this, true)

        a.recycle()
    }


    override fun onAttachedToWindow() {
        Log.d("swithun-xxxx", "MyView - onAttachedToWindow")
        super.onAttachedToWindow()
        findViewTreeViewModelStoreOwner()?.let { safeVMSO ->
            vm = ViewModelProvider(safeVMSO)[when (vmType) {
                VMType.CHILD_VM1 -> ChildViewModel1::class.java
                VMType.CHILD_VM2 -> ChildViewModel2::class.java
            }]
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
            shareVm?.shareStringFlow?.collect {
                b.shareStringChild.text = it
            }
        }
    }

}

enum class VMType(val value: Int) {
    CHILD_VM1(0),
    CHILD_VM2(1);
}

inline fun <reified T : Enum<T>> TypedArray.getEnum(index: Int, default: T) =
    getInt(index, -1).let {
        if (it >= 0) enumValues<T>()[it] else default
    }