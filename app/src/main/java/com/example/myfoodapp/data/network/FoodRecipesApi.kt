package com.example.myfoodapp.data.network

import com.example.myfoodapp.Constants.Companion.QUERY_API_KEY
import com.example.myfoodapp.models.FoodJoke
import com.example.myfoodapp.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface FoodRecipesApi {

    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipe>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String, String>
    ): Response<FoodRecipe>

    @GET("food/jokes/random")
    suspend fun getFoodJoke(
        @Query(QUERY_API_KEY) apiKey: String
    ): Response<FoodJoke>
}