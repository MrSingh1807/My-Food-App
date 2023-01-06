package com.example.myfoodapp.ui.fragments.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.example.myfoodapp.Constants.Companion.RECIPES_RESULT_KEY
import com.example.myfoodapp.R
import com.example.myfoodapp.bindingadapters.RecipesRowBinding
import com.example.myfoodapp.databinding.FragmentOverviewBinding
import com.example.myfoodapp.models.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val myBundle = args!!.getParcelable<Result>(RECIPES_RESULT_KEY) as Result


        binding.mainImgVw.load(myBundle.image) {
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        binding.titleTxtVw.text = myBundle.title
        binding.likesTxtVw.text = myBundle.aggregateLikes.toString()
        binding.timeTxtVw.text = myBundle.readyInMinutes.toString()

        RecipesRowBinding.parseHtml(binding.summaryTxtVw, myBundle.summary)

        updateColor(myBundle.vegetarian, binding.vegetarianTxtVw, binding.vegetarianImgVw)
        updateColor(myBundle.vegan, binding.veganTxtVw, binding.veganImgVw)
        updateColor(myBundle.glutenFree, binding.glutenFreeTxtVw, binding.glutenFreeImgVw)
        updateColor(myBundle.dairyFree, binding.dairyFreeTxtVw, binding.dairyFreeImgVw)
        updateColor(myBundle.veryHealthy, binding.healthyTxtVw, binding.healthyImgVw)
        updateColor(myBundle.cheap, binding.cheapTxtVw, binding.cheapImgVw)
    }

    private fun updateColor(stateIsOn: Boolean, textView: TextView, imageView: ImageView) {
        if (stateIsOn){
            imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}