package com.example.recipes.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipes.model.FoodJoke
import com.example.recipes.util.Constants.Companion.FOOD_JOKE_TABLE
import kotlinx.android.parcel.RawValue

@Entity (tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity (
    @Embedded
    val foodJoke : FoodJoke // as we can save complex objects ,
    // embedded annotation will inspect model class and takes it contents which is just string
        ) {
    @PrimaryKey(autoGenerate = false)
    var id  :Int = 0
}