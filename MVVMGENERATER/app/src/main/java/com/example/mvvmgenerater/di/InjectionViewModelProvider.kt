package com.example.mvvmgenerater.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class InjectionViewModelProvider<VM : ViewModel> @Inject constructor(
    private val lazyViewModel: dagger.Lazy<VM>
) {

    @Suppress("UNCHECKED_CAST")
    val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>) = lazyViewModel.get() as T
    }

    inline fun <reified VM : ViewModel, ACTIVITY : FragmentActivity> get(activity: ACTIVITY): VM {
        return ViewModelProvider(activity, viewModelFactory).get(VM::class.java)
    }

    inline fun <reified VM : ViewModel, FRAGMENT : Fragment> get(fragment: FRAGMENT): VM {
        return ViewModelProvider(fragment, viewModelFactory).get(VM::class.java)
    }
}