package com.example.testmvvm.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testmvvm.ui.vm.MainViewModel

class MainViewModelFactory(private val countReserved: Int) : ViewModelProvider.Factory {
    //6
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(countReserved) as T //7
    }
}