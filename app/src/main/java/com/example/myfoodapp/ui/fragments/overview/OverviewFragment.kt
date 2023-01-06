package com.example.myfoodapp.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.example.myfoodapp.Constants.Companion.RECIPES_RESULT_KEY
import com.example.myfoodapp.R
import com.example.myfoodapp.databinding.FragmentOverviewBinding
import com.example.myfoodapp.models.Result
import dagger.hilt.android.AndroidEntryPoint
import org.jsoup.Jsoup

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

        val args =  arguments
        val myBundle: Result? = args?.getParcelable(RECIPES_RESULT_KEY)


        binding.mainImgVw.load(myBundle?.image){
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        binding.titleTxtVw.text = myBundle?.title
        binding.likesTxtVw.text = myBundle?.aggregateLikes.toString()
        binding.timeTxtVw.text = myBundle?.readyInMinutes.toString()
        myBundle?.summary.let {
            val summery = Jsoup.parse(it).text() ?: "Empty Summery"
            binding.summaryTxtVw.text = summery
        }

        if(myBundle?.vegetarian == true){
            binding.vegetarianImgVw.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.vegetarianTxtVw.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }

        if(myBundle?.vegan == true){
            binding.veganImgVw.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.veganTxtVw.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }

        if(myBundle?.glutenFree == true){
            binding.glutenFreeImgVw.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.glutenFreeTxtVw.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if(myBundle?.dairyFree == true){
            binding.dairyFreeImgVw.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
           binding.dairyFreeTxtVw.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if(myBundle?.veryHealthy == true){
            binding.healthyImgVw.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.healthyTxtVw.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if(myBundle?.cheap == true){
            binding.cheapImgVw.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.cheapTxtVw.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}