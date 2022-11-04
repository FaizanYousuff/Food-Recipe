package com.example.recipes.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipes.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoriteRecipeEntity (
    @PrimaryKey(autoGenerate = true)
    var id :Int,
    var result : com.example.recipes.model.Result
    ){
}