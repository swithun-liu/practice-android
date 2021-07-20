package com.example.doubanmovie

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MovieTypeAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<MovieTypeAdapter.ViewHolder>() {

    // listener
    private lateinit var onItemClickListener: OnItemClickListener

    // 选中item变色
    private var selectedPosition: Int = 0

    private val TAG_movie_type = "MainActivity Movie Type"

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieTypeText: TextView = itemView.findViewById(R.id.movieTypeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_type, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movieTypeText.text = dataSet[position]

        // 选中item变色
        if (position == selectedPosition) {
            holder.movieTypeText.setTextColor(Color.WHITE)
            holder.movieTypeText.setBackgroundColor(Color.BLACK)
        }
        else {
            holder.movieTypeText.setTextColor(Color.BLACK)
            holder.movieTypeText.setBackgroundColor(Color.WHITE)
        }

        // listener
        holder.movieTypeText.setOnClickListener{
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
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

}
