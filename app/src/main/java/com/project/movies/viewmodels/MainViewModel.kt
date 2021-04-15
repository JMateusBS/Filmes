package com.project.movies.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.project.movies.data.Repository
import com.project.movies.data.database.entities.FavoritesEntity
import com.project.movies.data.database.entities.MoviesEntity
import com.project.movies.model.Movies
import com.project.movies.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class MainViewModel @ViewModelInject constructor(
    private val  repository: Repository,
    application: Application
)  : AndroidViewModel(application) {

    /** ROOM DATABASE **/
    val readMovies: LiveData<List<MoviesEntity>> = repository.local.readMovies().asLiveData()
    val readFavoriteMovies: LiveData<List<FavoritesEntity>> = repository.local.readFavoriteMovies().asLiveData()

    private fun insertMovies(moviesEntity: MoviesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertMovies(moviesEntity)
        }

    fun insertFavoriteMovie(favoritesEntity: FavoritesEntity)=
        viewModelScope.launch(Dispatchers.IO){
            repository.local.insertFavoriteMovies(favoritesEntity)
        }

    fun deleteFavoriteMovie(favoritesEntity: FavoritesEntity)=
        viewModelScope.launch(Dispatchers.IO){
            repository.local.deleteFavoriteMovie(favoritesEntity)
        }

     fun deleteAllFavoriteMovies() =
        viewModelScope.launch(Dispatchers.IO){
            repository.local.deleteAllFavoriteMovies()
        }


    /** RETROFIT **/
    var moviesResponse: MutableLiveData<NetworkResult<Movies>> = MutableLiveData()

    fun getMovies(queries: Map<String, String>) = viewModelScope.launch {
        getMoviesSafeCall(queries)
    }

    private suspend fun getMoviesSafeCall(queries: Map<String, String>) {
        moviesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.getMovies(queries)
                moviesResponse.value = handleMoviesResponse(response)

                val movies = moviesResponse.value!!.data
                if (movies != null){
                    offlineCacheMovies(movies)
                }
            }catch (e: Exception){
                moviesResponse.value = NetworkResult.Error("Movies not found.")
            }

        } else {
            moviesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheMovies(movies: Movies) {
        val moviesEntity = MoviesEntity(movies)
        insertMovies(moviesEntity)
    }

    private fun handleMoviesResponse(response: Response<Movies>): NetworkResult<Movies>? {
        when{
            response.message().toString().contains("timeout")->{
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val movies = response.body()
                return NetworkResult.Success(movies!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}