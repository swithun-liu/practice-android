package com.example.doubanmovie.ui.main.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doubanmovie.data.api.ApiHelper
import com.example.doubanmovie.data.api.ApiServiceImpl
import com.example.doubanmovie.databinding.ActivityMainBinding
import com.example.doubanmovie.service.DownloadService
import com.example.doubanmovie.ui.base.ViewModelFactory
import com.example.doubanmovie.ui.main.adapters.MovieCardAdapter
import com.example.doubanmovie.ui.main.adapters.MovieTypeAdapter
import com.example.doubanmovie.ui.main.viewmodels.MovieItemViewModel
import com.example.doubanmovie.ui.main.viewmodels.MovieTypeViewModel

private const val MSG_SAY_HELLO = 1
private const val MSG_DOWNLOAD_COVER = 2

class MovieView(
    private var mainActivity: MainActivity, private val binding: ActivityMainBinding,
) {
    private val tag = "MainActivity"
    private var currentMovieTag = "热门"

    private lateinit var movieItemViewModel: MovieItemViewModel
    private lateinit var movieTypeViewModel: MovieTypeViewModel

    private var apiServiceImpl: ApiServiceImpl = ApiServiceImpl()
    private var apiHelper: ApiHelper = ApiHelper(apiServiceImpl)

    private var mService: Messenger? = null
    private var bound = false

    private var connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(tag, "onServiceConnected")
            mService = Messenger(service)
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(tag, "onServiceDisconnected")
            mService = null
            bound = false
        }
    }

    init {
        binding.clockView3.start()
        initView()
        initAction()
    }

    private fun initView() {
        setUpViewModel()
        initMovieList()
        initMovieType()

    }

    fun downloadCover(coverUrl: String, movieName: String) {
        val msg = Message.obtain(null, MSG_DOWNLOAD_COVER, 0, 0)
        val bundle = Bundle()
        bundle.putString("coverUrl", coverUrl)
        bundle.putString("movieName", movieName)
        msg.data = bundle
        mService?.send(msg)
    }

    private fun initAction() { // initialize form device
        movieTypeViewModel.getMovieTypeData(mainActivity)
        movieItemViewModel.getMovieDataFromFile(mainActivity, currentMovieTag)

        // initialize from Internet
        movieTypeViewModel.getMovieTypeFromInternet()
        movieItemViewModel.getMovieItemsFromInternet(null, null, true)

        Intent(mainActivity, DownloadService::class.java).also {
            mainActivity.bindService(it, connection, Context.BIND_AUTO_CREATE)
        }
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
                downloadCover(
                    movieItemViewModel.episode.value?.get(position)?.cover ?: "",
                    movieItemViewModel.episode.value?.get(position)?.title ?: ""
                )
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

                val intent = Intent(mainActivity, DownloadService::class.java)
                mainActivity.bindService(intent, connection, Context.BIND_AUTO_CREATE)
                mainActivity.stopService(intent)
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