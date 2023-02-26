package com.mfy.recipes.ui.details.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.mfy.recipes.R
import com.mfy.recipes.util.Constants
import kotlinx.android.synthetic.main.fragment_overview.view.*
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        val args = arguments
        val bundle : com.mfy.recipes.model.Result? = args?.getParcelable(Constants.RESULT_BUNDLE_KEY)

        view.main_imageView.load(bundle?.image)
        view.like_textView.text = bundle?.aggregateLikes.toString()
        view.time_textView.text = bundle?.readyInMinutes.toString()
        view.title_text_view.text = bundle?.title.toString()
        view.summary_text_view.text = bundle?.summary.toString()

        bundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            view.summary_text_view.text = summary
        }

        if(bundle?.vegetarian == true){
            view.vegetarian_image_view.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.vegetarian_text_view.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if(bundle?.vegan == true){
            view.vegan_image_view.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.vegan_text_view.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }


        if(bundle?.glutenFree == true){
            view.gluten_free.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.gluten_free_text_view.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }


        if(bundle?.dairyFree == true){
            view.dairy_free_image_view.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.dairy_free_text_view.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }


        if(bundle?.veryHealthy == true){
            view.healthy_image_View.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.healthy_text_view.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }


        if(bundle?.cheap == true){
            view.cheap_image_view.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.cheap_text_view.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }




        return view
    }

}