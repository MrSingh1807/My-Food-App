package com.example.myfoodapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfoodapp.data.database.entites.FavoritesEntity
import com.example.myfoodapp.data.database.entites.FoodJokeEntity
import com.example.myfoodapp.data.database.entites.RecipesEntity
import com.example.myfoodapp.models.FoodJoke
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipesDAO {

    /****** All Recipes *******/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    /****** Favourite Recipes *******/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteRecipes(favouriteEntity: FavoritesEntity)

    @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavouriteRecipes(): Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavouriteRecipe(favouriteEntity: FavoritesEntity)

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavouriteRecipes()

    /****** Food Joke *******/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM food_joke_table ORDER BY id ASC")
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>
}