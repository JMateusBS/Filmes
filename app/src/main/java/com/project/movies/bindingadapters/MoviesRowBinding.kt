package com.project.movies.bindingadapters

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.project.movies.R
import com.project.movies.model.Result
import com.project.movies.ui.fragments.movies.MoviesFragmentDirections
import com.project.movies.util.Constants.Companion.IMAGE_URL
import java.lang.Exception

class MoviesRowBinding {

    companion object{

        @BindingAdapter("onMoviesClickListener")
        @JvmStatic
        fun onMoviesClickListener(moviesRowLayout: ConstraintLayout, result: Result){
            moviesRowLayout.setOnClickListener {
                try {
                    val action =
                        MoviesFragmentDirections.actionMoviesFragmentToDetailsActivity(result)
                    moviesRowLayout.findNavController().navigate(action)
                }catch (e: Exception){
                    Log.d("onMoviesClickListener", e.toString())
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String){
            imageView.load(IMAGE_URL+imageUrl){
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun  setNumberOfLikes(textView: TextView, likes: Double){
            textView.text = likes.toString()
        }
    }
}