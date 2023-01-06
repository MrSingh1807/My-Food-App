package com.example.myfoodapp.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfoodapp.Constants.Companion.RECIPES_RESULT_KEY
import com.example.myfoodapp.adapters.IngredientsAdapter
import com.example.myfoodapp.databinding.FragmentIngredientsBinding
import com.example.myfoodapp.models.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngredientsFragment : Fragment() {

    private val mAdapter : IngredientsAdapter by lazy { IngredientsAdapter() }

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args =  arguments
        val myBundle: Result? = args?.getParcelable(RECIPES_RESULT_KEY)

        setUpRecyclerView()
        myBundle?.extendedIngredients?.let { mAdapter.setData(it) }
    }

    private fun setUpRecyclerView(){
        binding.ingredientsRecyclerView.adapter = mAdapter
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}