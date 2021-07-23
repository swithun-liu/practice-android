package com.example.mvvmgenerater.di.modules

import com.example.mvvmgenerater.test1Activity
import com.example.mvvmgenerater.test1Module
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityInjectorsModule {

    @ContributesAndroidInjector(modules = [test1Module::class])
    abstract fun test1ActivityInjector(): test1Activity
}