package com.mfy.recipes.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mfy.recipes.model.FoodRecipe
import com.mfy.recipes.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipeEntity(
    var foodRecipe: FoodRecipe
    ) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}