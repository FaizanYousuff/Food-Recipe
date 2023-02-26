package com.mfy.recipes.data.network

import com.mfy.recipes.model.FoodJoke
import com.mfy.recipes.model.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipeApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(@QueryMap queries: Map<String, String>): Response<FoodRecipe>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipe(@QueryMap queries: Map<String, String>) : Response<FoodRecipe>

    @GET("food/jokes/random")
    suspend fun getFoodJoke(@Query("apiKey") apiKey : String) : Response<FoodJoke>
}