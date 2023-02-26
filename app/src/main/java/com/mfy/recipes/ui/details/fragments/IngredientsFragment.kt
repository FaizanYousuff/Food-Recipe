package com.mfy.recipes.ui.details.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfy.recipes.R
import com.mfy.recipes.adpaters.IngredientsAdapter
import com.mfy.recipes.util.Constants
import kotlinx.android.synthetic.main.fragment_ingredients.view.*


class IngredientsFragment : Fragment() {

    private val adapter : IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)

        val args = arguments
        val myBundle : com.mfy.recipes.model.Result? = args?.getParcelable(Constants.RESULT_BUNDLE_KEY)

        setupRecyclerView(view)
        myBundle?.extendedIngredients?.let {
            adapter.setData(it)
        }

        return view
    }

    fun setupRecyclerView(view :View){
        view.ingredients_recycler_view.adapter = adapter
        view.ingredients_recycler_view.layoutManager = LinearLayoutManager(requireContext())
    }


}