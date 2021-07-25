package com.example.doubanmovie.ui.main.view

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doubanmovie.data.api.ApiHelper
import com.example.doubanmovie.data.api.ApiServiceImpl
import com.example.doubanmovie.databinding.ActivityMainBinding
import com.example.doubanmovie.ui.base.ViewModelFactory
import com.example.doubanmovie.ui.main.adapters.MovieCardAdapter
import com.example.doubanmovie.ui.main.adapters.MovieTypeAdapter
import com.example.doubanmovie.ui.main.viewmodels.MovieItemViewModel
import com.example.doubanmovie.ui.main.viewmodels.MovieTypeViewModel

class MovieView(
    private var mainActivity: MainActivity, private val binding: ActivityMainBinding,
) {
    private val tag = "MainActivity"
    private var currentMovieTag = "热门"

    private lateinit var movieItemViewModel: MovieItemViewModel
    private lateinit var movieTypeViewModel: MovieTypeViewModel

    private var apiServiceImpl: ApiServiceImpl = ApiServiceImpl()
    private var apiHelper: ApiHelper = ApiHelper(apiServiceImpl)

    init {
        initView()
        initAction()
    }

    private fun initView() {
        setUpViewModel()
        initMovieList()
        initMovieType()
    }

    private fun initAction() { // initialize form device
        movieTypeViewModel.getMovieTypeData(mainActivity)
        movieItemViewModel.getMovieDataFromFile(mainActivity, currentMovieTag)

        // initialize from Internet
        movieTypeViewModel.getMovieTypeFromInternet()
        movieItemViewModel.getMovieItemsFromInternet(null, null, true)
    }

    private fun setUpViewModel() {
        movieTypeViewModel = ViewModelProvider(mainActivity, ViewModelFactory(apiHelper)).get(
            MovieTypeViewModel::class.java
        )
        movieItemViewModel = ViewModelProvider(mainActivity, ViewModelFactory(apiHelper)).get(
            MovieItemViewModel::class.java
        )
    }

    private fun initMovieList() {

        // 1. setup layoutManager
        val movieItemLinearLayoutManager = LinearLayoutManager(mainActivity)
        binding.movieItemList.layoutManager = movieItemLinearLayoutManager

        // 2. create adapter
        val movieCardAdapter = MovieCardAdapter()
        movieCardAdapter.setOnItemClickListener(object : MovieCardAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.d(tag, "点击图片")
                val intent = Intent(mainActivity, MovieDetailActivity::class.java)
                intent.putExtra("url", movieItemViewModel.episode.value?.get(position)?.url)
                mainActivity.startActivity(intent)
            }

            override fun onItemLongClick(view: View, position: Int) {
                Log.d(tag, "长按图片")
            }
        })
        binding.movieItemList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (movieItemLinearLayoutManager.findLastCompletelyVisibleItemPosition() == movieItemViewModel.episode.value?.size?.minus(
                        1
                    ) ?: 0
                ) {
                    Log.d(tag, "得加载了")
                    movieItemViewModel.getMovieItemsFromInternet(
                        currentMovieTag, movieItemViewModel.episode.value?.size, false
                    )
                }
            }
        }) // 3. setup adapter
        binding.movieItemList.adapter = movieCardAdapter

        movieItemViewModel.episode.observe(mainActivity, {
            Log.d(tag, "MovieItem 触发观察")
            movieCardAdapter.setDataSet(it)
            binding.movieItemList.adapter?.notifyDataSetChanged() //
        })

    }

    private fun initMovieType() {

        // 1. setup layoutManager
        binding.movieTypeList.layoutManager = LinearLayoutManager(mainActivity).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }

        // 2. create adapter
        val movieTypeAdapter = MovieTypeAdapter()
        movieTypeAdapter.setOnItemClickListener(object : MovieTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) { // 选中item变色
                movieTypeAdapter.setSelectedPosition(position)
                movieTypeViewModel.refresh()
                currentMovieTag = movieTypeViewModel.movieTypes.value?.get(position).toString()
                Log.d(tag, "点击${currentMovieTag}")
                movieItemViewModel.getMovieItemsFromInternet(currentMovieTag, null, true)
            }

            override fun onItemLongClick(view: View, position: Int) {
                Log.d(tag, "长按${movieItemViewModel.episode.value?.get(position)}")
            }
        })

        // 3. setup adapter
        binding.movieTypeList.adapter = movieTypeAdapter
        movieTypeViewModel.movieTypes.observe(mainActivity, {
            movieTypeAdapter.setDataSet(it)
            binding.movieTypeList.adapter?.notifyDataSetChanged()
        })

    }
}