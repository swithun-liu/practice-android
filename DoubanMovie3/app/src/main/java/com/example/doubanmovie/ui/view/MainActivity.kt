package com.example.doubanmovie.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doubanmovie.databinding.ActivityMainBinding
import com.example.doubanmovie.ui.adapter.MovieCardAdapter
import com.example.doubanmovie.ui.adapter.MovieTypeAdapter
import com.example.doubanmovie.ui.viewmodel.MovieItemViewModel
import com.example.doubanmovie.ui.viewmodel.MovieTypeViewModel

class MainActivity : AppCompatActivity() {


    lateinit var movieItemViewModel: MovieItemViewModel
    lateinit var movieTypeViewModel: MovieTypeViewModel

    private val tag = "MainActivity"
    private val tagMovieType = "MainActivity MovieType"
    private lateinit var binding: ActivityMainBinding

    private var currentMovieTag = "热门"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // movieItem
        val movieItemLinearLayoutManager = LinearLayoutManager(this)
        binding.movieItemList.layoutManager = movieItemLinearLayoutManager

        movieItemViewModel = ViewModelProvider(this).get(MovieItemViewModel::class.java)
        movieTypeViewModel = ViewModelProvider(this).get(MovieTypeViewModel::class.java)

        val movieCardAdapter = MovieCardAdapter()
        movieCardAdapter.setOnItemClickListener(object : MovieCardAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.d(tag, "点击图片")
                val intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
                intent.putExtra("url", movieItemViewModel.episode.value?.get(position)?.url)
                startActivity(intent)
            }

            override fun onItemLongClick(view: View, position: Int) {
                Log.d(tag, "长按图片")
            }
        })
        // movieItem 加载更多
        binding.movieItemList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (movieItemLinearLayoutManager.findLastCompletelyVisibleItemPosition() == movieItemViewModel.episode.value?.size?.minus(
                        1
                    ) ?: 0) {
                    Log.d(tag, "得加载了")
                    movieItemViewModel.getMovieItemsFromInternet(currentMovieTag, movieItemViewModel.episode.value?.size, false)
                }
            }
        })
        binding.movieItemList.adapter = movieCardAdapter
        movieItemViewModel.episode.observe(this, Observer {
            Log.d(tag, "MovieItem 触发观察")
            movieCardAdapter.setDataSet(it)
            binding.movieItemList.adapter?.notifyDataSetChanged() //
        })


        // movieType
        binding.movieTypeList.layoutManager =
            LinearLayoutManager(this).also { it.orientation = LinearLayoutManager.HORIZONTAL }

        val movieTypeAdapter = MovieTypeAdapter()

        movieTypeAdapter.setOnItemClickListener(object : MovieTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) { // 选中item变色
                movieTypeAdapter.setSelectedPosition(position)
                movieTypeViewModel.setMovieTypes()
                currentMovieTag = movieTypeViewModel.movieTypes.value?.get(position).toString()
                Log.d(tagMovieType, "点击${currentMovieTag}")
                movieItemViewModel.getMovieItemsFromInternet(currentMovieTag, null, true)
            }

            override fun onItemLongClick(view: View, position: Int) {
                Log.d(tagMovieType, "长按${movieItemViewModel.episode.value?.get(position)}")
            }
        })
        binding.movieTypeList.adapter = movieTypeAdapter
        movieTypeViewModel.movieTypes.observe(this, Observer {
            movieTypeAdapter.setDataSet(it)
            binding.movieTypeList.adapter?.notifyDataSetChanged()
        })


        // initialize form device
        movieTypeViewModel.getMovieTypeData(this) //        getMovieData(this, currentMovieTag)
        movieItemViewModel.getMovieDataFromDisk(this, currentMovieTag)

        // initialize from Internet
        movieTypeViewModel.getMovieTypeFromInternet()
        movieItemViewModel.getMovieItemsFromInternet(null, null, true)

    }

}