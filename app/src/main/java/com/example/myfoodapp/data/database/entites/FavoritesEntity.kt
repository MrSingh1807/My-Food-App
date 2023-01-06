package com.example.myfoodapp.data.database.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myfoodapp.Constants.Companion.FAVOURITE_RECIPES_TABLE
import com.example.myfoodapp.models.Result


@Entity(tableName = FAVOURITE_RECIPES_TABLE)
data class FavoritesEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)