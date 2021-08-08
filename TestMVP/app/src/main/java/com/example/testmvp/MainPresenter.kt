package com.example.testmvp

class MainPresenter(private val mView: MainContract.View) : MainContract.Presenter {
    override fun loadData() {
        val s = "我是从服务器取得的数据"
        mView.onDataLoaded(s)
    }

    override fun functionA() {
        // do Something
        // then 通知view更新
        mView.onFunctionAFinished()
    }

}