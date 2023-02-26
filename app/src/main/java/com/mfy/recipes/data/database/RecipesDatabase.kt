package com.mfy.recipes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mfy.recipes.data.database.entities.FavoriteRecipeEntity
import com.mfy.recipes.data.database.entities.FoodJokeEntity
import com.mfy.recipes.data.database.entities.RecipeEntity


// TODO later need to Revisit
@Database(entities = [RecipeEntity::class, FavoriteRecipeEntity::class, FoodJokeEntity::class],
    version = 2 ,
    exportSchema = false
)
@TypeConverters (RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao() : RecipesDao
}