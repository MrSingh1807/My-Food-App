package com.example.myfoodapp.bindingadapters

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodapp.adapters.FavouriteRecipesAdapter
import com.example.myfoodapp.data.database.entites.FavoritesEntity

class FavouriteRecipesBinding {

    companion object {

        @BindingAdapter("viewVisibility", "setData", requireAll = false)

        @JvmStatic
        fun setVisibility(
            view: View,
            favouriteEntity: List<FavoritesEntity>?,
            favAdapter: FavouriteRecipesAdapter?
        ){
            when(view){
                is RecyclerView -> {
                    val dataCheck = favouriteEntity.isNullOrEmpty()
                    view.isInvisible = dataCheck
                    if (!dataCheck){
                        favouriteEntity?.let { favAdapter?.setData(it) }
                    }
                }
                else -> view.isVisible = favouriteEntity.isNullOrEmpty()
            }
        }
    }
}