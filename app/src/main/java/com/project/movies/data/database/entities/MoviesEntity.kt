package com.project.movies.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.movies.model.Movies
import com.project.movies.util.Constants.Companion.MOVIES_TABLE

@Entity(tableName = MOVIES_TABLE)
class MoviesEntity (
        var movies: Movies
){
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}