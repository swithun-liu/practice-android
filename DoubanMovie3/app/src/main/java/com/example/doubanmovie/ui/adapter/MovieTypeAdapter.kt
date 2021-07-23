package com.example.doubanmovie.ui.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doubanmovie.R.id
import com.example.doubanmovie.R.layout
import com.example.doubanmovie.ui.adapter.MovieTypeAdapter.ViewHolder

class MovieTypeAdapter() : RecyclerView.Adapter<ViewHolder>() {

    // 选中item变色
    private var selectedPosition: Int = 0
    private val TAG_movie_type = "MainActivity Movie Type"

    private var dataSet: List<String> = mutableListOf()

    // listener
    private lateinit var onItemClickListener: OnItemClickListener

    fun setDataSet(data: MutableList<String>) {
        Log.d("MainActivityHH1", data.toString())
        dataSet = data
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieTypeText: TextView = itemView.findViewById(id.movieTypeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout.movie_type, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("MainActivityHH", dataSet[position])
        holder.movieTypeText.text = dataSet[position]

        // 选中item变色
        if (position == selectedPosition) {
            holder.movieTypeText.setTextColor(Color.WHITE)
            holder.movieTypeText.setBackgroundColor(Color.BLACK)
        } else {
            holder.movieTypeText.setTextColor(Color.BLACK)
            holder.movieTypeText.setBackgroundColor(Color.WHITE)
        }

        // listener
        holder.movieTypeText.setOnClickListener {
            onItemClickListener?.onItemClick(holder.itemView, position)
        }
        holder.movieTypeText.setOnLongClickListener {
            onItemClickListener?.onItemLongClick(holder.itemView, position)
            true
        }
    }

    // 选中item变色
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
    }

    fun getSelectedPosition(): Int {
        return selectedPosition
    }

    override fun getItemCount() = dataSet.size

    // listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    // listener
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

}
