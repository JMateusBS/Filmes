package com.project.movies.data

import com.project.movies.data.database.MoviesDAO
import com.project.movies.data.database.entities.FavoritesEntity
import com.project.movies.data.database.entities.MoviesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
        private val moviesDAO: MoviesDAO
) {

    fun readMovies(): Flow<List<MoviesEntity>>{
        return moviesDAO.readMovies()
    }

    fun readFavoriteMovies(): Flow<List<FavoritesEntity>>{
        return moviesDAO.readFavoriteMovies()
    }

    suspend fun insertMovies(moviesEntity: MoviesEntity){
        moviesDAO.insertMovies(moviesEntity)
    }

    suspend fun insertFavoriteMovies(favoritesEntity: FavoritesEntity){
        moviesDAO.insertFavoriteMovie(favoritesEntity)
    }

    suspend fun deleteFavoriteMovie(favoritesEntity: FavoritesEntity){
        moviesDAO.deleteFavoriteMovie(favoritesEntity)
    }

    suspend fun deleteAllFavoriteMovies(){
        moviesDAO.deleteAllFavoriteMovies()
    }

}