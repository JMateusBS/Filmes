package com.project.movies.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.project.movies.data.database.entities.MoviesEntity
import com.project.movies.model.Movies
import com.project.movies.util.NetworkResult

class MoviesBinding {

    companion object {
        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
                imageView: ImageView,
                apiResponse: NetworkResult<Movies>?,
                database: List<MoviesEntity>?
        ){
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                imageView.visibility = View.VISIBLE
            } else if (apiResponse is NetworkResult.Loading){
                imageView.visibility = View.INVISIBLE
            } else if (apiResponse is NetworkResult.Success){
                imageView.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("readApiResponseText", "readDatabaseText", requireAll = true)
        @JvmStatic
        fun errorTextViewVisibility(
                textView: TextView,
                apiResponse: NetworkResult<Movies>?,
                database: List<MoviesEntity>?
        ){
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if (apiResponse is NetworkResult.Loading){
                textView.visibility = View.INVISIBLE
            } else if (apiResponse is NetworkResult.Success){
                textView.visibility = View.INVISIBLE
            }
        }
    }
}