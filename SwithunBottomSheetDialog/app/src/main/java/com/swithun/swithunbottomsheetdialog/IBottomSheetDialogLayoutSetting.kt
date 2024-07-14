package com.swithun.swithunbottomsheetdialog

interface IBottomSheetDialogLayoutSetting {

    /** 手动初始化，方便继承，避免初始化时使用abstract变量 */
    fun init()

    /** 自定义的吸附位置列表 */
    var customStateList: List<BottomSheetDialogLayout.CustomHeight>?

    /** 初始化时到哪个state */
    var initState: Int

    /** state变化监听 */
    var stateListener: ((Int) -> Unit)?

    fun asIBottomSheetDialogLayoutSetting(): IBottomSheetDialogLayoutSetting = this

}