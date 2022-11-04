package com.example.recipes.model

import com.google.gson.annotations.SerializedName

data class FoodJoke(
    @SerializedName("text")
    val text : String
) {
}