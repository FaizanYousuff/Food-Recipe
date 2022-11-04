package com.example.recipes.bindingAdpaters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.recipes.R
import com.example.recipes.data.database.entities.RecipeEntity
import com.example.recipes.model.FoodRecipe
import com.example.recipes.ui.fragments.recipe.RecipesFragmentDirections
import com.example.recipes.util.NetworkResult
import org.jsoup.Jsoup

class RecipesBinding {
    companion object {

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(rowLayout : ConstraintLayout, result : com.example.recipes.model.Result){
            rowLayout.setOnClickListener{
                try {
                    val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                    rowLayout.findNavController().navigate(action)

                } catch (e : java.lang.Exception){
                    Log.e("onRecipeClickListener"," "+e.message.toString())
                }
            }
        }

        @BindingAdapter("readApiResponse","readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
            imageView: ImageView,
            apiResponse : NetworkResult<FoodRecipe>?,
            database : List<RecipeEntity>?
        ){
            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                imageView.visibility = View.VISIBLE
            } else if(apiResponse is NetworkResult.Loading || apiResponse is NetworkResult.Success){
                imageView.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("readApiResponse2","readDatabase2", requireAll = true)
        @JvmStatic
        fun errorTextViewVisibility(
            textView: TextView,
            apiResponse : NetworkResult<FoodRecipe>?,
            database : List<RecipeEntity>?
        ){
            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if(apiResponse is NetworkResult.Loading || apiResponse is NetworkResult.Success){
                textView.visibility = View.INVISIBLE
            }
        }



        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView , imageUrl :String){
            imageView.load(imageUrl){
                crossfade(600).error(R.drawable.ic_error)
            }

        }

        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun setNumberOfLikes(textView: TextView, likeS: Int) {
            textView.text = likeS.toString()

        }

        @BindingAdapter("setNumberOfMinutes")
        @JvmStatic
        fun setNumberOfMinutes(textView: TextView, minutes: Int) {
            textView.text = minutes.toString()

        }
        @BindingAdapter("isVegan")
        @JvmStatic
        fun isVegan(view: View, isVegan: Boolean) {
            if (isVegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
                    }
                    is ImageView -> {
                        view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
                    }
                }
            }
        }

        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView ,description : String?){
            if(description != null){
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }
    }
}