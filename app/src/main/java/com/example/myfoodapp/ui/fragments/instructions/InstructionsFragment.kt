package com.example.myfoodapp.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.myfoodapp.Constants
import com.example.myfoodapp.R
import com.example.myfoodapp.databinding.FragmentInstructionsBinding
import com.example.myfoodapp.models.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args =  arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPES_RESULT_KEY)

        binding.instructionsWebVw.webViewClient = object : WebViewClient() { }

        val webSiteURl : String = myBundle!!.sourceUrl
        binding.instructionsWebVw.loadUrl(webSiteURl)
    }

}