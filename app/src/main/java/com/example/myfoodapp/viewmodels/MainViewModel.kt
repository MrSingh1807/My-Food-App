package com.example.myfoodapp.viewmodels


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Parcelable
import androidx.lifecycle.*
import com.example.myfoodapp.data.database.entites.FavoritesEntity
import com.example.myfoodapp.data.database.entites.FoodJokeEntity
import com.example.myfoodapp.data.database.entites.RecipesEntity
import com.example.myfoodapp.models.FoodJoke
import com.example.myfoodapp.models.FoodRecipe
import com.example.myfoodapp.repositories.Repository
import com.example.myfoodapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
   @ApplicationContext private val context: Context
) : ViewModel() {

    /*****   ROOM DATABASE   *****/

    val readRecipes : LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipes : LiveData<List<FavoritesEntity>> = repository.local.readFavouriteRecipes().asLiveData()
    val readFoodJoke : LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()


    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch (Dispatchers.IO){
            repository.local.insertRecipes(recipesEntity)
        }
    fun insertFavouriteRecipes(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch (Dispatchers.IO){
            repository.local.insertFavouriteRecipes(favoritesEntity)
    }
    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) = viewModelScope.launch {
        repository.local.insertFoodJoke(foodJokeEntity)
    }

    fun deleteFavouriteRecipes(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch (Dispatchers.IO){
            repository.local.deleteFavouriteRecipe(favoritesEntity)
        }
    fun deleteAllFavouriteRecipes() =
        viewModelScope.launch (Dispatchers.IO){
            repository.local.deleteAllFavouriteRecipes()
        }

    /*****   RETROFIT   *****/
    var recipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchRecipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var foodJokeResponse : MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }
    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }
    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }



    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if (foodRecipe != null){
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: java.lang.Exception){
                recipesResponse.value = NetworkResult.Error("Recipes Not Found")
            }
        } else{
            recipesResponse.value = NetworkResult.Error ("No Internet Connection...")
        }
    }
    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: java.lang.Exception){
                searchRecipesResponse.value = NetworkResult.Error("Recipes Not Found")
            }
        } else{
            searchRecipesResponse.value = NetworkResult.Error ("No Internet Connection...")
        }
    }
    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)

                val foodJoke = foodJokeResponse.value!!.data
                if (foodJoke != null){
                    offlineCacheFoodJoke(foodJoke)
                }
            } catch (e: java.lang.Exception){
                foodJokeResponse.value = NetworkResult.Error("Recipes Not Found")
            }
        } else{
            foodJokeResponse.value = NetworkResult.Error ("No Internet Connection...")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }
    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when{
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Connection TimeOut")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API key Limited")
            }
            response.body()?.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes Not Found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes)
            }
            else -> {
               return NetworkResult.Error(response.message())
            }
        }
    }
    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
        return when{
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Connection TimeOut")
            }
            response.code() == 402 -> {
                NetworkResult.Error("API key Limited")
            }
            response.isSuccessful -> {
                val foodJoke = response.body()
                NetworkResult.Success(foodJoke)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =  connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }

}