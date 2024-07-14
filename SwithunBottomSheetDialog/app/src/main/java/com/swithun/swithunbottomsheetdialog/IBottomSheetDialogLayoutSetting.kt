package com.swithun.swithunbottomsheetdialog

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

interface IBottomSheetDialogLayoutSetting {

    /** 手动初始化，方便继承，避免初始化时使用abstract变量 */
    fun init()

    /** 自定义的吸附位置列表 */
    var customStateList: List<BottomSheetDialogLayout.CustomHeight>?

    /** 初始化时到哪个state */
    var initState: Int

    /** state变化监听 */
    var stateListener: ((Int) -> Unit)?

    /** 背景蒙层 */
    var bgMask: Drawable

    fun asIBottomSheetDialogLayoutSetting(): IBottomSheetDialogLayoutSetting = this

}