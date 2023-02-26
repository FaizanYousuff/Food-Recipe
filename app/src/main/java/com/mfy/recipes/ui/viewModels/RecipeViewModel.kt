package com.mfy.recipes.ui.viewModels

import android.app.Application
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mfy.recipes.data.database.DatastoreRepository
import com.mfy.recipes.util.Constants.Companion.API_KEY
import com.mfy.recipes.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.mfy.recipes.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.mfy.recipes.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.mfy.recipes.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.mfy.recipes.util.Constants.Companion.QUERY_API_KEY
import com.mfy.recipes.util.Constants.Companion.QUERY_DIET
import com.mfy.recipes.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.mfy.recipes.util.Constants.Companion.QUERY_NUMBER
import com.mfy.recipes.util.Constants.Companion.QUERY_TYPE
import com.mfy.recipes.util.Constants.Companion.SEARCH_QUERY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class RecipeViewModel @ViewModelInject constructor(
    application: Application,
    private val datastore : DatastoreRepository
    ) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    val readMealAndDietType =  datastore.readMealAndDietType
    val readBackOnline = datastore.readBackOnline.asLiveData()

    var networkStatus = false
    var backOnline = false

    fun saveMealAndDietType(mealType :String , mealTypeId :Int, dietType: String , dietTypeId :Int){
        viewModelScope.launch(Dispatchers.IO) {
            datastore.saveMealAndDiet(mealType,mealTypeId,dietType,dietTypeId)

        }

    }
    fun saveBackOnline(backOnline: Boolean){
        viewModelScope.launch(Dispatchers.IO){
            datastore.saveBackOnline(backOnline)
        }
    }

     fun getApiQueries() : HashMap<String, String>{

         viewModelScope.launch {
             datastore.readMealAndDietType.collect {values ->
                 mealType = values.selectedMealType
                 dietType = values.selectedDietType
             }
         }
        val queries : HashMap<String,String> = HashMap()
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

    fun getSearchApiQueries(query: String) : HashMap<String, String>{


        val queries : HashMap<String,String> = HashMap()
        queries[SEARCH_QUERY] = query
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

    fun showNetworkStatus(){
        if(!networkStatus){
            Toast.makeText(getApplication(),"No internet connection",Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else {
            if(backOnline){
                Toast.makeText(getApplication(),"We are Back Online.",Toast.LENGTH_SHORT).show()
                saveBackOnline(false)

            }
        }
    }

}