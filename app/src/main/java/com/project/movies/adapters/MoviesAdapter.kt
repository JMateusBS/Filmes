package com.project.movies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.movies.databinding.MoviesRowLayoutBinding
import com.project.movies.model.Movies
import com.project.movies.model.Result
import com.project.movies.util.MoviesDiffUtil

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MyViewHolder>() {

    private var movies = emptyList<Result>()

    class MyViewHolder(private val binding: MoviesRowLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result){
            binding.result = result
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MoviesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMovies = movies[position]
        holder.bind(currentMovies)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setData(newData: Movies){
        val movieDiffUtil = MoviesDiffUtil(movies, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(movieDiffUtil)
        movies = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}