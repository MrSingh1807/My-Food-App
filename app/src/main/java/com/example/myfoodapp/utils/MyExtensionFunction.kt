package com.example.myfoodapp.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>){
    observe(lifecycleOwner, object : Observer<T>{
        override fun onChanged(t: T) {
            removeObserver(this)
            observer.onChanged(t)
        }
//      Use of this function --> WHEN we start our app first time we want that data is fetch directly to server
    //      avoid readDatabase() function call in RecipesFragment Class
//  Why would I call readDatabase() fun when I already know that, that fun is empty
    })
}