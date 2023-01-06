package com.example.myfoodapp.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.myfoodapp.Constants.Companion.RECIPES_RESULT_KEY
import com.example.myfoodapp.R
import com.example.myfoodapp.adapters.PagerAdapter
import com.example.myfoodapp.data.database.entites.FavoritesEntity
import com.example.myfoodapp.databinding.ActivityDetailsBinding
import com.example.myfoodapp.ui.fragments.ingredients.IngredientsFragment
import com.example.myfoodapp.ui.fragments.instructions.InstructionsFragment
import com.example.myfoodapp.ui.fragments.overview.OverviewFragment
import com.example.myfoodapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var menuItem: MenuItem

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var recipesSaved = false
    private var savedRecipeID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolBar)
        binding.toolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("OverView")
        titles.add("Ingredient")
        titles.add("Instruction")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPES_RESULT_KEY, args.result)

        val pagerAdapter = PagerAdapter(resultBundle, fragments, titles, supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.saveToFavouritesMenu)
        checkSavedRecipes(menuItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.saveToFavouritesMenu && !recipesSaved) {
            saveToFavourites(item)
        } else if (item.itemId == R.id.saveToFavouritesMenu && recipesSaved) {
            removeFromFavourites(item)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this) {
            try {
                for (savedRecipe in it) {
                    if (savedRecipe.result.id == args.result.id) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeID = savedRecipe.id
                        recipesSaved = true
                    }
                }
            } catch (e: Exception) {
                Log.d("Details Activity", e.message.toString())
            }
        }
    }

    private fun saveToFavourites(item: MenuItem) {
        val favouriteEntity = FavoritesEntity(0, args.result)
        mainViewModel.insertFavouriteRecipes(favouriteEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe Saved")
        recipesSaved = true
    }

    private fun removeFromFavourites(item: MenuItem) {
        val favouriteEntity = FavoritesEntity(savedRecipeID, args.result)
        mainViewModel.deleteFavouriteRecipes(favouriteEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favourites. ")
        recipesSaved = false

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.detailsLayout, message, Snackbar.LENGTH_SHORT).setAction("Okey") {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon?.setTint(ContextCompat.getColor(this, color))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem, R.color.white)
    }
}