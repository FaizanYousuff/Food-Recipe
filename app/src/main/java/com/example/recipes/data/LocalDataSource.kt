package com.example.recipes.data

import com.example.recipes.data.database.entities.RecipeEntity
import com.example.recipes.data.database.RecipesDao
import com.example.recipes.data.database.entities.FavoriteRecipeEntity
import com.example.recipes.data.database.entities.FoodJokeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
    ) {

     fun readRecipes() : Flow<List<RecipeEntity>>{
        return recipesDao.readRecipes()
    }

    suspend fun insertRecipes(recipeEntity: RecipeEntity){
        recipesDao.insertRecipes(recipeEntity)
    }

    fun readFavoriteRecipesDatabase() : Flow<List<FavoriteRecipeEntity>>{
        return recipesDao.readFavoriteRecipes()
    }


    suspend fun insertFavoriteRecipes(favoriteRecipeEntity: FavoriteRecipeEntity){
        recipesDao.insertFavoriteRecipes(favoriteRecipeEntity)
    }
    suspend fun deleteFavoriteRecipes(favoriteRecipeEntity: FavoriteRecipeEntity){
        recipesDao.deleteFavoriteRecipe(favoriteRecipeEntity)
    }


    suspend fun deleteALlFavoriteRecipes(){
        recipesDao.deleteAllFavoriteRecipes()
    }



    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        recipesDao.insertFoodJoke(foodJokeEntity)
    }

    fun readFoodJokes() : Flow<List<FoodJokeEntity>>{
        return recipesDao.readFoodJokes()
    }

}