package com.example.mvvmgenerater

import com.example.mvvmgenerater.R
import javax.inject.Inject
import android.os.Bundle
import com.example.mvvmgenerater.di.qualifiers.ViewModelInjection
import com.example.mvvmgenerater.ui.BaseActivity

class test1Activity : BaseActivity() {

    @Inject
    @ViewModelInjection
    lateinit var viewModel: test1VM

    override fun layoutRes() = R.layout.activity_test1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}