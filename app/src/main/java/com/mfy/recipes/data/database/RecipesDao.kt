package com.mfy.recipes.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfy.recipes.data.database.entities.FavoriteRecipeEntity
import com.mfy.recipes.data.database.entities.FoodJokeEntity
import com.mfy.recipes.data.database.entities.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM RECIPES_TABLE ORDER BY id ASC")
     fun readRecipes() : Flow<List<RecipeEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipes(favoriteRecipeEntity: FavoriteRecipeEntity)

    @Query("SELECT * FROM FAVORITE_RECIPES_TABLE ORDER BY id ASC")
    fun readFavoriteRecipes() : Flow<List<FavoriteRecipeEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoriteRecipeEntity: FavoriteRecipeEntity)

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity:FoodJokeEntity)

    @Query("SELECT * FROM FOOD_JOKE_TABLE ORDER BY id ASC")
    fun readFoodJokes() : Flow<List<FoodJokeEntity>>



}