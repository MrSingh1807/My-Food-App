package com.example.myfoodapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myfoodapp.data.database.entites.FavoritesEntity
import com.example.myfoodapp.data.database.entites.FoodJokeEntity
import com.example.myfoodapp.data.database.entites.RecipesEntity

@Database(
    entities = [RecipesEntity::class,FavoritesEntity::class,FoodJokeEntity::class],
    version = 1,
    exportSchema = false)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {


    abstract fun recipesDAO(): RecipesDAO

}