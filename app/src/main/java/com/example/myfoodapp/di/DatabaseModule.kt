package com.example.myfoodapp.di

import android.content.Context
import androidx.room.Room
import com.example.myfoodapp.Constants.Companion.DATABASE_NAME
import com.example.myfoodapp.data.database.RecipesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, RecipesDatabase::class.java, DATABASE_NAME).build()


    @Singleton
    @Provides
    fun provideDAO(database: RecipesDatabase) = database.recipesDAO()

}