package com.example.recipes.ui.fragments.recipe.bottomSheet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.recipes.R
import com.example.recipes.ui.viewModels.RecipeViewModel
import com.example.recipes.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.recipes.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.recipes_bottom_sheet.view.*


class RecipesBottomSheet : BottomSheetDialogFragment() {

    private  val TAG = "RecipesBottomSheet"

    private lateinit var recipeViewModel: RecipeViewModel

    private var mealType = DEFAULT_MEAL_TYPE
    private var mealTypeId = 0
    private var dietType = DEFAULT_DIET_TYPE
    private var dietTypeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.recipes_bottom_sheet, container, false)

        recipeViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) { values ->
            mealType = values.selectedMealType
            dietType = values.selectedDietType
            updateChip(values.selectedMealTypeId, view.meal_type_chip_group)
            updateChip(values.selectedDietTypeId, view.diet_type_chip_group)

        }
        view.meal_type_chip_group.setOnCheckedChangeListener{group , selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            mealType = chip.text.toString().toLowerCase()
            mealTypeId = selectedChipId
        }

        view.diet_type_chip_group.setOnCheckedChangeListener{group , selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            dietType = chip.text.toString().toLowerCase()
            dietTypeId = selectedChipId
        }


        view.apply_btn.setOnClickListener{
            recipeViewModel.saveMealAndDietType(
                mealType,
            mealTypeId,
            dietType,
            dietTypeId)
            val actions = RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(actions)
        }
        return view
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if(chipGroup != null){
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            }catch (e :java.lang.Exception){
                Log.e(TAG,e.message.toString())
            }

        }

    }

}