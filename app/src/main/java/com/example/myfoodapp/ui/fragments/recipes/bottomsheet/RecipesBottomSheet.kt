package com.example.myfoodapp.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.myfoodapp.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.myfoodapp.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.myfoodapp.R
import com.example.myfoodapp.databinding.RecipesBottomSheetBinding
import com.example.myfoodapp.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*

class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel

    private var _binding: RecipesBottomSheetBinding? = null
    private val binding get() =  _binding!!

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipID = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = RecipesBottomSheetBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeID, binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeID, binding.dietTypeChipGroup)
        }

       binding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipID ->
            // later Replace this function with OnCheckedStateChangeListener()
            val chip = group.findViewById<Chip>(selectedChipID)
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            Log.d("Tag", selectedMealType +" & " + selectedChipID)
            mealTypeChip = selectedMealType
            mealTypeChipID = selectedChipID
        }
        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipID ->
            val chip = group.findViewById<Chip>(selectedChipID)
            val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            Log.d("Tag", selectedDietType +" & " + selectedChipID)
            dietTypeChip = selectedDietType
            dietTypeChipID = selectedChipID
        }
        binding.applyBtn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipID,
                dietTypeChip,
                dietTypeChipID
            )
            val action = RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }

    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: java.lang.Exception) {
                Log.d("RecipesBottomSheet", e.message.toString())
            }
        }
    }
}