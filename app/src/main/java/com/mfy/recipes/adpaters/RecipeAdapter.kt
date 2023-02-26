package com.mfy.recipes.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mfy.recipes.databinding.RecipesRowLayoutBinding
import com.mfy.recipes.model.FoodRecipe
import  com.mfy.recipes.model.Result
import com.mfy.recipes.util.RecipeDiffUtil

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipes = emptyList<Result>()

    class RecipeViewHolder(private val binding: RecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(result :Result) {
                binding.result = result
                // this will update our layout when ever there is change
                binding.executePendingBindings()
            }

        // Below method will return object of recipe View holder from outside
        companion object {
            fun from (parent : ViewGroup) : RecipeViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipesRowLayoutBinding.inflate(layoutInflater,parent,false)
                return RecipeViewHolder(binding )

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
       return RecipeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
       val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    /**
     * This method will be called from Fragment to update/ Set data
     */
    fun setData(newData : FoodRecipe){
        recipes = newData.results
        val recipesDiffUtil = RecipeDiffUtil(recipes,newData.results)
        val diffUtil = DiffUtil.calculateDiff(recipesDiffUtil)
        diffUtil.dispatchUpdatesTo(this)
    }
}