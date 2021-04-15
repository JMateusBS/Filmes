package com.project.movies.util

class Constants {

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = "f72e4dd641ec74c5e6fa6b5f3caea947"

        //API Query Keys
        const val QUERY_API_KEY = "api_key"


        //ROOM Database
        const val DATABASE_NAME = "movies_database"
        const val MOVIES_TABLE = "movies_table"
        const val FAVORITE_MOVIES_TABLE = "favorite_movies_table"

    }
}