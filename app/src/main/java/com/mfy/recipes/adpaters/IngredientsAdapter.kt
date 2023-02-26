package com.mfy.recipes.adpaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mfy.recipes.R
import com.mfy.recipes.model.ExtendedIngredient
import com.mfy.recipes.util.Constants.Companion.BASE_IMAGE_URL
import com.mfy.recipes.util.RecipeDiffUtil
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredients_row_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.itemView.ingredients_image_view.load(BASE_IMAGE_URL+ingredientsList[position].image){
           crossfade(600 )
           error(R.drawable.ic_error)
       }

        holder.itemView.ingredients_amount.text = ingredientsList[position].amount.toString()
        holder.itemView.ingredients_name.text = ingredientsList[position].name.capitalize()
        holder.itemView.ingredients_unit.text = ingredientsList[position].unit
        holder.itemView.ingredients_consistency.text = ingredientsList[position].consistency
        holder.itemView.ingredients_original.text = ingredientsList[position].original
     }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredientData: List<ExtendedIngredient>){
        val  ingredientsDiffUtil =
            RecipeDiffUtil(ingredientsList,newIngredientData)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newIngredientData
        diffUtilResult.dispatchUpdatesTo(this)


    }
}