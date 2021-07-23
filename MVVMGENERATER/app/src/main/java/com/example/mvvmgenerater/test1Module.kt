package com.example.mvvmgenerater

import dagger.Module
import dagger.Provides
import com.example.mvvmgenerater.di.qualifiers.ViewModelInjection
import com.example.mvvmgenerater.di.InjectionViewModelProvider

@Module
class test1Module {

    @Provides
    @ViewModelInjection
    fun providetest1VM(
        activity: test1Activity, viewModelProvider: InjectionViewModelProvider<test1VM>
    ): test1VM = viewModelProvider.get(activity)
}