package com.project.movies.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.movies.model.Movies
import com.project.movies.model.Result

class MoviesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun movieToString(movie: Movies): String {
        return gson.toJson(movie)
    }

    @TypeConverter
    fun stringToMovies(data: String): Movies {
        val listType = object : TypeToken<Movies>(){}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun resulttoString(result: Result): String{
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String): Result{
        val listType = object : TypeToken<Result>(){}.type
        return gson.fromJson(data, listType)
    }
}