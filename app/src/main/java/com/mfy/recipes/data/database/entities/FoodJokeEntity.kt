package com.mfy.recipes.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mfy.recipes.model.FoodJoke
import com.mfy.recipes.util.Constants.Companion.FOOD_JOKE_TABLE

@Entity (tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity (
    @Embedded
    val foodJoke : FoodJoke // as we can save complex objects ,
    // embedded annotation will inspect model class and takes it contents which is just string
        ) {
    @PrimaryKey(autoGenerate = false)
    var id  :Int = 0
}