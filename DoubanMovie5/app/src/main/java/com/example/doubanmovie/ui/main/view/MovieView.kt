package com.example.doubanmovie.ui.main.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.view.View
import android.widget.Toast
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

private const val MSG_REGISTER_CLIENT = 0
private const val MSG_SAY_HELLO = 1
private const val MSG_DOWNLOAD_COVER = 2 // 用与注册Activity

class MovieView(
    private var mainActivity: MainActivity, private val binding: ActivityMainBinding,
) {
    private val tag = "MainActivity"
    private var currentMovieTag = "热门"

    private lateinit var movieItemViewModel: MovieItemViewModel
    private lateinit var movieTypeViewModel: MovieTypeViewModel

    private var apiServiceImpl: ApiServiceImpl = ApiServiceImpl()
    private var apiHelper: ApiHelper = ApiHelper(apiServiceImpl)

    // sendMsg to Service
    private var activityMessenger: Messenger? = null
    private var bound = false

    // acceptMsg from Service
    private val serviceMessenger = Messenger(InComingHandler(mainActivity))

    // sendMsg to Service
    private var connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(tag, "onServiceConnected")
            activityMessenger = Messenger(service)
            bound = true
            val message = Message().also {
                it.what = MSG_REGISTER_CLIENT
                it.replyTo = serviceMessenger
            }
            activityMessenger?.send(message)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(tag, "onServiceDisconnected")
            activityMessenger = null
            bound = false
        }
    }

    init {
        initView()
        initAction()
    }

    // acceptMsg from Service
    internal class InComingHandler(private val context: Context) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_DOWNLOAD_COVER -> Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {
        setUpViewModel()
        initClock()
        initMovieList()
        initMovieType()
    }

    private fun initClock() {
        binding.clockView3.start()
    }

    fun downloadCover(coverUrl: String, movieName: String) { // sendMsg
        val msg = Message.obtain(null, MSG_DOWNLOAD_COVER, 0, 0)
        val bundle = Bundle().also {
            it.putString("coverUrl", coverUrl)
            it.putString("movieName", movieName)
        }
        msg.data = bundle
        activityMessenger?.send(msg)
    }

    private fun initAction() {

        // initialize form device
        movieTypeViewModel.getMovieTypeFromFile(mainActivity)
        movieItemViewModel.getMovieDataFromFile(mainActivity, currentMovieTag)

        if (deviceIsOnline()) { // initialize from Internet
            movieTypeViewModel.getMovieTypeFromInternet(mainActivity)
            movieItemViewModel.getMovieItemsFromInternet(
                null, null, clearAll = true, save = true, context = mainActivity
            )
        }

        Intent(mainActivity, DownloadService::class.java).also {
            mainActivity.bindService(it, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun deviceIsOnline(): Boolean {
        val connMgr =
            mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return (networkInfo?.isConnected == true).also {
            Log.d(tag, "${if (it) "" else "不"}在线")
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
                Log.d(
                    tag,
                    "长按图片 - ${movieItemViewModel.episode.value?.get(position)?.cover ?: ""} - ${
                        movieItemViewModel.episode.value?.get(position)?.title ?: ""
                    }"
                )
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
                    if (deviceIsOnline()) {
                        movieItemViewModel.getMovieItemsFromInternet(
                            currentMovieTag,
                            movieItemViewModel.episode.value?.size,
                            false,
                            save = true,
                            context = mainActivity
                        )
                    }
                }
            }
        })

        // 3. setup adapter
        binding.movieItemList.adapter = movieCardAdapter

        movieItemViewModel.episode.observe(mainActivity, {
            movieCardAdapter.setDataSet(it)
            binding.movieItemList.adapter?.notifyDataSetChanged() //
            //            movieItemViewModel.setMovieDataToFile(mainActivity, currentMovieTag)
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
                movieItemViewModel.getMovieDataFromFile(mainActivity, currentMovieTag)
                if (deviceIsOnline()) {
                    movieItemViewModel.clearAll()
                    movieItemViewModel.getMovieItemsFromInternet(
                        currentMovieTag, null, clearAll = true, save = true, context = mainActivity
                    )
                }
            }

            override fun onItemLongClick(view: View, position: Int) {
                Log.d(tag, "长按${movieItemViewModel.episode.value?.get(position)}")
                val intent = Intent(mainActivity, DownloadService::class.java)
                mainActivity.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
        })

        // 3. setup adapter
        binding.movieTypeList.adapter = movieTypeAdapter
        movieTypeViewModel.movieTypes.observe(mainActivity, {
            movieTypeAdapter.setDataSet(it)
            binding.movieTypeList.adapter?.notifyDataSetChanged()
        })

    }

    fun onStop() {
        mainActivity.unbindService(connection)
    }
}