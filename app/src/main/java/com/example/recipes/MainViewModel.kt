package com.example.recipes

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipes.data.Repository
import com.example.recipes.data.database.entities.FavoriteRecipeEntity
import com.example.recipes.data.database.entities.FoodJokeEntity
import com.example.recipes.data.database.entities.RecipeEntity
import com.example.recipes.model.FoodJoke
import com.example.recipes.model.FoodRecipe
import com.example.recipes.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {


    /**
     * Local database
     */

     var recipesFromDatabase = repository.local.readRecipes().asLiveData()
    val favoriteRecipeFromDatabase = repository.local.readFavoriteRecipesDatabase().asLiveData()
    val readFoodJokes = repository.local.readFoodJokes().asLiveData()




    private fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodJoke(foodJokeEntity)
        }
    }

    private fun insertRecipes(recipeEntity: RecipeEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipeEntity)
        }
    }

     fun insertFavoriteRecipes(favoriteRecipeEntity: FavoriteRecipeEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoriteRecipeEntity)
        }
    }

     fun deleteFavoriteRecipe(favoriteRecipeEntity: FavoriteRecipeEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipes(favoriteRecipeEntity)
        }
    }

     fun deleteAllFavoriteRecipes(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteALlFavoriteRecipes()
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe){
        val recipeEntity = RecipeEntity(foodRecipe)
        insertRecipes(recipeEntity)
    }

    private fun offlineCacheFoodJokes(foodJoke: FoodJoke){
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }



    /**
     * RETROFIT
     */

    var recipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchedRecipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var  fooJokeResponse :  MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    /**
     * This method will be called from Activity / Fragment to get recipes
     */


    fun getFoodJoke(apiKey : String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }

    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        fooJokeResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response =   repository.remote.getFoodJoke(apiKey)
                fooJokeResponse.value = handleFoodJokeResponse(response)
                val foodJoke = fooJokeResponse.value!!.data
                if(foodJoke != null){
                    offlineCacheFoodJokes(foodJoke)
                }
            } catch (e : java.lang.Exception){
                fooJokeResponse.value = NetworkResult.Error("Food Joke Not Found")
            }

        } else {

            fooJokeResponse.value = NetworkResult.Error("No Internet Connection ")
        }

    }

    fun getRecipes(queries:Map<String,String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipe(queries : Map<String,String>) = viewModelScope.launch {
        getSearchRecipesSafeCall(queries)
    }

    private suspend fun getSearchRecipesSafeCall(queries: Map<String, String>) {
        searchedRecipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response =   repository.remote.searchQuery(queries)
                searchedRecipesResponse.value = handleFoodRecipesResponse(response)

            } catch (e : java.lang.Exception){
                searchedRecipesResponse.value = NetworkResult.Error("Recipes Not Found")
            }

        } else {

            searchedRecipesResponse.value = NetworkResult.Error("No Internet Connection ")
        }

    }

    /**
     * This will validate internet connection and update loading status
     */
    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
             val response =   repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe != null){
                    offlineCacheRecipes(foodRecipe )
                }
            } catch (e : java.lang.Exception){
                recipesResponse.value = NetworkResult.Error("Recipes Not Found")
            }

        } else {

            recipesResponse.value = NetworkResult.Error("No Internet Connection ")
        }


    }


    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {

        when {
            response.message().contains("timeout") ->{
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 ->{
                return NetworkResult.Error("API key Limited")
            }
            response.isSuccessful ->{
                return NetworkResult.Success(response.body())
            }
            else ->{
                return  NetworkResult.Error(response.message())
            }
        }

    }
    /**
     * This will handle negative response of API
     */
    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {

        when {
            response.message().contains("timeout") ->{
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 ->{
                return NetworkResult.Error("API key Limited")
            }
            response.body()!!.results.isNullOrEmpty() ->{
                return NetworkResult.Error("Recipes Not Found")
            }
            response.isSuccessful ->{
                return NetworkResult.Success(response.body())
            }
            else ->{
              return  NetworkResult.Error(response.message())
            }
        }

    }


    /**
     * This function returns true if Internet available  if not it will return false
     */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork ?: return false
        } else {
            TODO("VERSION.SDK_INT < M")
            return false
        }
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false

        }

    }
}