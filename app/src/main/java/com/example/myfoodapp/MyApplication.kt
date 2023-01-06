package com.example.myfoodapp

import android.app.Application
import com.example.myfoodapp.di.NetworkModule
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@HiltAndroidApp
class MyApplication @Inject constructor() : Application() {

}
