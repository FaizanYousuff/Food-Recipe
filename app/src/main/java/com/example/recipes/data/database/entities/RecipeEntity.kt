package com.example.recipes.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipes.model.FoodRecipe
import com.example.recipes.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipeEntity(
    var foodRecipe: FoodRecipe
    ) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}