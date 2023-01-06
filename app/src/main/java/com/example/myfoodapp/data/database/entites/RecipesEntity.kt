package com.example.myfoodapp.data.database.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myfoodapp.Constants.Companion.RECIPES_TABLE
import com.example.myfoodapp.models.FoodRecipe
import javax.inject.Inject

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity @Inject constructor (
   var foodRecipe: FoodRecipe) {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
    }