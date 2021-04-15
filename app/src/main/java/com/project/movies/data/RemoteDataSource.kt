package com.project.movies.data

import com.project.movies.data.network.MoviesApi
import com.project.movies.model.Movies
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val movieApi: MoviesApi
) {

    suspend fun getMovies(queries: Map<String, String>): Response<Movies>{
        return movieApi.getPopular(queries)
    }
}