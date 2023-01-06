package com.example.myfoodapp.bindingadapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.example.myfoodapp.data.database.entites.FoodJokeEntity
import com.example.myfoodapp.models.FoodJoke
import com.example.myfoodapp.utils.NetworkResult

class FoodJokeBinding {

    companion object {

        @BindingAdapter(
            "readApiResponse_forFoodJoke",
            "readDatabase_forFoodJoke",
            requireAll = false
        )
        @JvmStatic
        fun setCardAndProgressBarVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            when (apiResponse) {
                is NetworkResult.Loading -> {
                    when (view) {
                        is ProgressBar -> view.visibility = View.VISIBLE
                        is CardView -> view.visibility = View.INVISIBLE
                    }
                }
                is NetworkResult.Error -> {
                    when (view) {
                        is ProgressBar -> view.visibility = View.INVISIBLE
                        is CardView -> {
                            view.visibility = View.VISIBLE
                            if (database != null) {
                                if (database.isNotEmpty()) {
                                    view.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Success -> {
                    when (view) {
                        is ProgressBar -> view.visibility = View.INVISIBLE
                        is CardView -> view.visibility = View.VISIBLE
                    }
                }
                else -> {}
            }

        }


        @BindingAdapter(
            "readApiResponse_forFoodJokeError",
            "readDatabase_forFoodJokeError",
            requireAll = true
        )
        @JvmStatic
        fun setErrorViewVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            if (database != null){
                if (database.isEmpty()) {
                    view.visibility = View.VISIBLE
                    if (view is TextView) {
                        view.text = apiResponse?.message.toString()
                    }
                }
            }
            if (apiResponse is NetworkResult.Success){
                view.visibility = View.INVISIBLE
            } else if(apiResponse is NetworkResult.Loading){
                view.visibility = View.INVISIBLE
            }
            else {
                view.visibility = View.VISIBLE
            }
        }
    }
}