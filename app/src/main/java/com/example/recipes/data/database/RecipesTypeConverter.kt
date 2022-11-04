package com.example.recipes.data.database

import androidx.constraintlayout.solver.GoalRow
import androidx.room.TypeConverter
import com.example.recipes.model.FoodRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Objects

// TODO later need to Revisit
class RecipesTypeConverter {

     val gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe)  : String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data : String) : FoodRecipe {
        val listType = object : TypeToken<FoodRecipe>(){}.type
        return gson.fromJson(data,listType)

    }



    @TypeConverter
    fun resultToString(result: com.example.recipes.model.Result)  : String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringResult(data : String) : com.example.recipes.model.Result {
        val listType = object : TypeToken<com.example.recipes.model.Result>(){}.type
        return gson.fromJson(data,listType)

    }
}