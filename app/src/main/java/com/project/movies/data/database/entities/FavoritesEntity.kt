package com.project.movies.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.movies.model.Result
import com.project.movies.util.Constants.Companion.FAVORITE_MOVIES_TABLE

@Entity(tableName = FAVORITE_MOVIES_TABLE)
class FavoritesEntity (

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)