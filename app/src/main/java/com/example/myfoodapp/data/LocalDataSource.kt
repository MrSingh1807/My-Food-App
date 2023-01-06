package com.example.myfoodapp.data


import com.example.myfoodapp.data.database.RecipesDAO
import com.example.myfoodapp.data.database.entites.FavoritesEntity
import com.example.myfoodapp.data.database.entites.FoodJokeEntity
import com.example.myfoodapp.data.database.entites.RecipesEntity
import com.example.myfoodapp.models.FoodJoke
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDAO: RecipesDAO
) {

    /****** All Recipes *******/
    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDAO.readRecipes()
    }
    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDAO.insertRecipes(recipesEntity)
    }

    /****** Favourite Recipes *******/
    fun readFavouriteRecipes(): Flow<List<FavoritesEntity>> {
        return recipesDAO.readFavouriteRecipes()
    }
    suspend fun insertFavouriteRecipes(favoritesEntity: FavoritesEntity) {
        recipesDAO.insertFavouriteRecipes(favoritesEntity)
    }
    suspend fun deleteFavouriteRecipe(favoritesEntity: FavoritesEntity) {
        recipesDAO.deleteFavouriteRecipe(favoritesEntity)
    }
    suspend fun deleteAllFavouriteRecipes() {
        recipesDAO.deleteAllFavouriteRecipes()
    }

    /****** Food Joke *******/
    fun readFoodJoke() : Flow<List<FoodJokeEntity>>{
        return recipesDAO.readFoodJoke()
    }
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        recipesDAO.insertFoodJoke(foodJokeEntity)
    }
}