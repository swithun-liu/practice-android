package com.swithun.bottomsheetdialog

import android.content.Context
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class SwithunBottomSheetBehavior(context: Context): BottomSheetBehavior<FrameLayout>(context, null) {

    init {
        OvershootInterpolator
    }

}