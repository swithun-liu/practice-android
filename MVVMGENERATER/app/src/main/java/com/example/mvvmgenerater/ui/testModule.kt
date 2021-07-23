package com.example.mvvmgenerater.ui

import dagger.Module
import dagger.Provides
import com.example.mvvmgenerater.di.qualifiers.ViewModelInjection
import com.example.mvvmgenerater.di.InjectionViewModelProvider

@Module
class testModule {

    @Provides
    @ViewModelInjection
    fun providetestVM(
        activity: testActivity, viewModelProvider: InjectionViewModelProvider<testVM>
    ): testVM = viewModelProvider.get(activity)
}