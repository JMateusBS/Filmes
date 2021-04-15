package com.project.movies.data.network

import com.project.movies.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.time.temporal.TemporalQueries

interface MoviesApi {

    @GET(value = "movie/popular")
    suspend fun getPopular(@QueryMap queries: Map<String, String>):
            Response<Movies>


}