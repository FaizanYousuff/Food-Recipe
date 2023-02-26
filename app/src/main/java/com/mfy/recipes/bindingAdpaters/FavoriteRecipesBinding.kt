package com.mfy.recipes.bindingAdpaters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mfy.recipes.adpaters.FavoriteRecipeAdapter
import com.mfy.recipes.data.database.entities.FavoriteRecipeEntity
import com.mfy.recipes.ui.fragments.favorite.FavoriteRecipesFragmentDirections


class FavoriteRecipesBinding {

    companion object {



        @BindingAdapter("onFavoriteRecipeClickListener")
        @JvmStatic
        fun onFavoriteRecipeClickListener (rootLayout : ConstraintLayout, favoritesEntity: FavoriteRecipeEntity ){
            rootLayout.setOnClickListener{
                rootLayout.findNavController().navigate(FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(favoritesEntity.result))
            }

        }
        @BindingAdapter("viewVisibility","setData", requireAll = false )
        @JvmStatic
        fun setDataVisibility(view :View,favoritesEntity : List<FavoriteRecipeEntity>?,adapter : FavoriteRecipeAdapter?){

            if(favoritesEntity.isNullOrEmpty()){

                when(view){
                    is ImageView -> {
                        view.visibility = View.VISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.VISIBLE
                        view.text = "No Favorite Recipes"
                    }
                    is RecyclerView -> {
                        view.visibility = View.GONE
                    }

                }

            } else {
                when(view){
                    is ImageView -> {
                        view.visibility = View.GONE
                    }
                    is TextView -> {
                        view.visibility = View.GONE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        adapter?.setData(favoritesEntity)
                    }

                }
            }

        }
    }


}