package com.example.mvvmgenerater.di.components

import com.example.mvvmgenerater.App
import com.example.mvvmgenerater.di.modules.ActivityInjectorsModule
import com.example.mvvmgenerater.di.modules.FragmentInjectorsModule
import com.example.mvvmgenerater.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ActivityInjectorsModule::class, FragmentInjectorsModule::class, AppModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)

}