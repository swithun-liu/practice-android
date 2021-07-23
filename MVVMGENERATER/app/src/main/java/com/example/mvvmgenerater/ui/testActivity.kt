package com.example.mvvmgenerater.ui

import com.example.mvvmgenerater.R
import javax.inject.Inject
import android.os.Bundle
import com.example.mvvmgenerater.di.qualifiers.ViewModelInjection
import com.example.mvvmgenerater.ui.BaseActivity

class testActivity : BaseActivity() {

    @Inject
    @ViewModelInjection
    lateinit var viewModel: testVM

    override fun layoutRes() = R.layout.activity_test

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}