package com.example.testmvvm.ui.vm

import androidx.lifecycle.ViewModel

class MainViewModel(countReserved : Int) : ViewModel() {
    var counter = countReserved
}