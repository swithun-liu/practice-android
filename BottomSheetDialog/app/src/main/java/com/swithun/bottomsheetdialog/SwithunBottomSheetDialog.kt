package com.swithun.bottomsheetdialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SwithunBottomSheetDialog : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.swithun_bottom_sheet_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val container = view.parent as FrameLayout
        val lp = container.layoutParams as CoordinatorLayout.LayoutParams
        (lp.behavior as BottomSheetBehavior).isDraggable

        // 从 FrameLayout 中获取 AttributeSet

//        lp.behavior = SwithunBottomSheetBehavior(view.context)
        super.onViewCreated(view, savedInstanceState)

        container.post {
            container.requestLayout()
        }
    }

}