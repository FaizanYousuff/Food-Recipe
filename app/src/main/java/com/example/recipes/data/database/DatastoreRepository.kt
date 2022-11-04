package com.example.recipes.data.database

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.example.recipes.util.Constants.Companion.DATASTORE_NAME
import com.example.recipes.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.recipes.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.recipes.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.example.recipes.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.example.recipes.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.example.recipes.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.example.recipes.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DatastoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val selectedMealType =  preferencesKey<String>(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeID =  preferencesKey<Int>(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType =  preferencesKey<String>(PREFERENCES_DIET_TYPE)
        val selectedDietTypeID =  preferencesKey<Int>(PREFERENCES_DIET_TYPE_ID)
        val backOnline =  preferencesKey<Boolean>(PREFERENCES_BACK_ONLINE)

    }

    private val dataStore = context.createDataStore(name = DATASTORE_NAME)

    suspend fun saveBackOnline(online :Boolean){
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnline] = online
        }
    }

    val readBackOnline : Flow<Boolean> = dataStore.data.
    catch { exception ->
        if(exception is IOException){
            emit(emptyPreferences())
        } else{
            throw  exception
        }
    }.map { preference ->
        val backOnline = preference[PreferenceKeys.backOnline] ?: false
       backOnline

    }


    suspend fun saveMealAndDiet(mealType :String , mealTypeId :Int, dietType: String , dietTypeId :Int) {
        dataStore.edit { preferences ->

            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeID] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeID] = dietTypeId
        }

    }

    val readMealAndDietType : Flow<MealAndDietType> = dataStore.data.
    catch { exception ->
        if(exception is IOException){
            emit(emptyPreferences())
        } else{
            throw  exception
        }
    }.map { preference ->
        val selectedMealType = preference[PreferenceKeys.selectedMealType]?:DEFAULT_MEAL_TYPE
        val selectedMealTypeId = preference[PreferenceKeys.selectedMealTypeID]?: 0
        val selectedDietType = preference[PreferenceKeys.selectedDietType]?:DEFAULT_DIET_TYPE
        val selectedMDietTypeID = preference[PreferenceKeys.selectedDietTypeID]?:0
        MealAndDietType(selectedMealType,selectedMealTypeId,selectedDietType,selectedMDietTypeID)

    }


}

data class MealAndDietType (
    val selectedMealType :String,
    val selectedMealTypeId :Int,
    val selectedDietType: String,
    val selectedDietTypeId :Int
    )
