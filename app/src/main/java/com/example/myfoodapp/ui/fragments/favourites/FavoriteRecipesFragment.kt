package com.example.myfoodapp.ui.fragments.favourites

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodapp.R
import com.example.myfoodapp.adapters.FavouriteRecipesAdapter
import com.example.myfoodapp.databinding.FragmentFavoriteRecipesBinding
import com.example.myfoodapp.databinding.FragmentRecipesBinding
import com.example.myfoodapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private val favouriteRecipesAdapter: FavouriteRecipesAdapter by lazy {
        FavouriteRecipesAdapter(
            requireActivity(),
            mainViewModel
        )
    }

    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.favAdapter = favouriteRecipesAdapter
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.favorite_recipes_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.deleteAll_favouriteRecipes_menu) {
                    mainViewModel.deleteAllFavouriteRecipes()
                    showSnackBar()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setUpRecyclerView(binding.favouriteRecipesRecyclerView)
    }

    private fun showSnackBar() {
        Snackbar.make(binding.root, "All Recipes Removed", Snackbar.LENGTH_SHORT)
            .setAction("Okay") {}.show()
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = favouriteRecipesAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        favouriteRecipesAdapter.clearContextualActionMode()
    }
}