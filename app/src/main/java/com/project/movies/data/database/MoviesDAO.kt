package com.project.movies.data.database

import androidx.room.*
import com.project.movies.data.database.entities.FavoritesEntity
import com.project.movies.data.database.entities.MoviesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(moviesEntity: MoviesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(favoritesEntity: FavoritesEntity)

    @Query(value = "SELECT * FROM movies_table ORDER BY id ASC")
    fun readMovies(): Flow<List<MoviesEntity>>

    @Query(value = "SELECT * FROM favorite_movies_table ORDER BY id ASC")
    fun readFavoriteMovies(): Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavoriteMovie(favoritesEntity: FavoritesEntity)

    @Query(value = "DELETE FROM favorite_movies_table")
    suspend fun deleteAllFavoriteMovies()
}