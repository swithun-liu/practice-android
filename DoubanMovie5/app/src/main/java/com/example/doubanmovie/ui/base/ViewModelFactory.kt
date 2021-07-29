package com.example.doubanmovie.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.doubanmovie.data.api.ApiHelper
import com.example.doubanmovie.data.repository.MainRepository
import com.example.doubanmovie.ui.main.viewmodels.MovieItemViewModel
import com.example.doubanmovie.ui.main.viewmodels.MovieTypeViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieTypeViewModel::class.java)) {
            return MovieTypeViewModel(MainRepository(apiHelper)) as T
        }
        else if (modelClass.isAssignableFrom(MovieItemViewModel::class.java)) {
            return MovieItemViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}