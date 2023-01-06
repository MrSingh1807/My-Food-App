package com.example.myfoodapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.myfoodapp.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.myfoodapp.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.myfoodapp.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.example.myfoodapp.Constants.Companion.PREFERENCES_DIET_TYPE
import com.example.myfoodapp.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.example.myfoodapp.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.example.myfoodapp.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.example.myfoodapp.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
){

    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeID = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeID = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(PREFERENCES_NAME)

    suspend fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int){
        context.dataStore.edit {preferences ->
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeID] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeID] = dietTypeId
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean){
        context.dataStore.edit {
            it[PreferenceKeys.backOnline] = backOnline
        }
    }

    val readMealAndDietType : Flow<MealAndDietType> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeID] ?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeID] ?: 0

            MealAndDietType(   // return this dataType
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    val readBackOnline : Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline] ?: false
            backOnline
        }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeID: Int,
    val selectedDietType: String,
    val selectedDietTypeID : Int
)