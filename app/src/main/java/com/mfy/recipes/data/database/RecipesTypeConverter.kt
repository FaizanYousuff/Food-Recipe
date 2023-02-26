package com.mfy.recipes.data.database

import androidx.room.TypeConverter
import com.mfy.recipes.model.FoodRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
    fun resultToString(result: com.mfy.recipes.model.Result)  : String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringResult(data : String) : com.mfy.recipes.model.Result {
        val listType = object : TypeToken<com.mfy.recipes.model.Result>(){}.type
        return gson.fromJson(data,listType)

    }
}