package com.project.movies.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.project.movies.util.Constants
import com.project.movies.util.Constants.Companion.QUERY_API_KEY

class MoviesViewModel(application: Application): AndroidViewModel(application) {

    fun applyQueries(): HashMap<String, String>{
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_API_KEY] = Constants.API_KEY

        return queries

    }

}