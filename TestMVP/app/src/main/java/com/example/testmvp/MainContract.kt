package com.example.testmvp

interface MainContract {

    interface Presenter {
        fun loadData()
        fun functionA()
    }

    interface View {
        fun onDataLoaded(s: String)
        fun onFunctionAFinished()
    }
}