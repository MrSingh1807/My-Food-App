package com.example.myfoodapp.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodapp.R
import com.example.myfoodapp.data.database.entites.FavoritesEntity
import com.example.myfoodapp.databinding.FavoriteRecipesRowLayoutBinding
import com.example.myfoodapp.ui.fragments.favourites.FavoriteRecipesFragmentDirections
import com.example.myfoodapp.utils.RecipesDiffUtil
import com.example.myfoodapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar

class FavouriteRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavouriteRecipesAdapter.FavouriteViewHolder>(), ActionMode.Callback {

    private var multiSelection = false
    private var myViewHolders = arrayListOf<FavouriteViewHolder>()

    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var favouriteRecipes = emptyList<FavoritesEntity>()

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    class FavouriteViewHolder(val binding: FavoriteRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favouriteEntity: FavoritesEntity) {
            binding.favouritesEntity = favouriteEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FavouriteViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return FavouriteViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView

        val currentRecipe = favouriteRecipes[position]
        holder.bind(currentRecipe)
        saveItemStateOnScroll(holder, currentRecipe)

        /**
         * Single Click  listener
         * ***/
        holder.binding.favoriteRecipesRowLayout.setOnClickListener {

            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                        currentRecipe.result
                    )

                holder.itemView.findNavController().navigate(action)
            }
        }
        /**
         *  Long Click  listener
         * ***/
        holder.binding.favoriteRecipesRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                return@setOnLongClickListener true
            } else {
                applySelection(holder, currentRecipe)
                return@setOnLongClickListener true
            }
        }
    }

    private fun saveItemStateOnScroll(holder: FavouriteViewHolder, currentRecipes: FavoritesEntity){
        if (selectedRecipes.contains(currentRecipes)) {
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
        } else {
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
    }
    private fun applySelection(holder: FavouriteViewHolder, currentRecipes: FavoritesEntity) {
        if (selectedRecipes.contains(currentRecipes)) {
            selectedRecipes.remove(currentRecipes)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionTitle()
        } else {
            selectedRecipes.add(currentRecipes)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionTitle()
        }
    }

    private fun changeRecipeStyle(holder: FavouriteViewHolder, background: Int, strokeColor: Int) {
        holder.binding.favoriteRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                background
            )
        )
        holder.binding.favoriteRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionTitle() {
        when (selectedRecipes.size) {
            0 -> {
                mActionMode.finish()
                multiSelection = false
            }
            1 -> mActionMode.title = "${selectedRecipes.size} item selected"
            else -> mActionMode.title = "${selectedRecipes.size} items selected"

        }
    }

    override fun getItemCount(): Int {
        return favouriteRecipes.size
    }


    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favourites_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.deleteFavouriteRecipeMenu) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavouriteRecipes(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s removed.")
            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)

        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newFavouriteRecipes: List<FavoritesEntity>) {
        val favouriteRecipesDiffUtil = RecipesDiffUtil(favouriteRecipes, newFavouriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favouriteRecipesDiffUtil)
        favouriteRecipes = newFavouriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).setAction("Okey"){}.show()
    }

    fun clearContextualActionMode(){
        if (this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }
}