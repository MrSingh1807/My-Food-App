package com.example.myfoodapp.data.database.entites

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myfoodapp.Constants.Companion.FOOD_JOKE_TABLE
import com.example.myfoodapp.models.FoodJoke


@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(
    @Embedded
    var foodJoke: FoodJoke
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}