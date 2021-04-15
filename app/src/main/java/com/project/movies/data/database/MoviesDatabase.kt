package com.project.movies.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.project.movies.data.database.entities.FavoritesEntity
import com.project.movies.data.database.entities.MoviesEntity

@Database(
        entities = [MoviesEntity::class, FavoritesEntity::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(MoviesTypeConverter::class)
abstract class MoviesDatabase: RoomDatabase() {

    abstract fun  moviesDao(): MoviesDAO
}