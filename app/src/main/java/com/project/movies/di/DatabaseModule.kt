package com.project.movies.di

import android.content.Context
import android.graphics.Movie
import androidx.room.Room
import com.project.movies.data.database.MoviesDatabase
import com.project.movies.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
            context,
            MoviesDatabase::class.java,
            DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun privadeDao(database: MoviesDatabase) = database.moviesDao()
}