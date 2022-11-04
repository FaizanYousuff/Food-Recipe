package com.example.recipes.data

import com.example.recipes.data.network.FoodRecipeApi
import com.example.recipes.model.FoodJoke
import com.example.recipes.model.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipeApi: FoodRecipeApi
) {

   suspend fun getRecipes(queries : Map<String, String>) : Response<FoodRecipe> {
       return foodRecipeApi.getRecipes(queries)
    }

    suspend fun searchQuery(queries : Map<String, String>) : Response<FoodRecipe> {
        return foodRecipeApi.searchRecipe(queries)
    }

    suspend fun getFoodJoke(apiKey : String) : Response<FoodJoke> {
        return foodRecipeApi.getFoodJoke(apiKey)
    }
}