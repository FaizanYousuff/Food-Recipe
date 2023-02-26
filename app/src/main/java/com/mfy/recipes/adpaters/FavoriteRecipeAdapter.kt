package com.mfy.recipes.adpaters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mfy.recipes.MainViewModel
import com.mfy.recipes.R
import com.mfy.recipes.data.database.entities.FavoriteRecipeEntity
import com.mfy.recipes.databinding.FavoriteRecipeRowLayoutBinding
import com.mfy.recipes.ui.fragments.favorite.FavoriteRecipesFragmentDirections
import com.mfy.recipes.util.RecipeDiffUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_recipe_row_layout.view.*

class FavoriteRecipeAdapter(
    private val requireActivity: FragmentActivity,
    val mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavoriteRecipeAdapter.RecipeViewHolder>(), ActionMode.Callback {

    private lateinit var rootView :View

    private var multiSelection = false
    private var selectedRecipe = arrayListOf<FavoriteRecipeEntity>()
    private var myViewHolderList = arrayListOf<RecipeViewHolder>()
    private lateinit var actionMode: ActionMode

    private var favoriteRecipeEntity = emptyList<FavoriteRecipeEntity>()

    class RecipeViewHolder(private val binding: FavoriteRecipeRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteRecipeEntity: FavoriteRecipeEntity) {
            binding.favoriteEntity = favoriteRecipeEntity
            // this will update our layout when ever there is change
            binding.executePendingBindings()
        }

        // Below method will return object of recipe View holder from outside
        companion object {
            fun from(parent: ViewGroup): RecipeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                return RecipeViewHolder(binding)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        myViewHolderList.add(holder)

        rootView = holder.itemView.rootView
        val currentRecipe = favoriteRecipeEntity[position]
        holder.bind(currentRecipe)

        /**
         * Single Click listener
         */
        holder.itemView.favorites_recipes_row_layout.setOnClickListener() {


            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                holder.itemView.findNavController().navigate(
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity
                        (currentRecipe.result)
                )
            }

        }

        holder.itemView.favorites_recipes_row_layout.setOnLongClickListener {

            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else {
                multiSelection = false
                false
            }

        }


    }

    override fun getItemCount(): Int {
        return favoriteRecipeEntity.size
    }

    private fun applySelection(holder: RecipeViewHolder, currentRecipe: FavoriteRecipeEntity) {
        if (selectedRecipe.contains(currentRecipe)) {
            selectedRecipe.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.card_background_color, R.color.strokeColor)
            applyActionModeTitle()

        } else {
            selectedRecipe.add(currentRecipe)
            changeRecipeStyle(holder, R.color.card_background_light_color, R.color.colorPrimary)
            applyActionModeTitle()

        }

    }

    private fun changeRecipeStyle(
        holder: RecipeViewHolder,
        backgroundColor: Int,
        strokeColor: Int
    ) {
        holder.itemView.favorites_recipes_row_layout
            .setBackgroundColor(ContextCompat.getColor(requireActivity, backgroundColor))
        holder.itemView.favorite_card_view_background_color.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle(){
        when(selectedRecipe.size){
            0 -> {
                actionMode.finish()
            }
            1 -> {
                actionMode.title = "${selectedRecipe.size} item Selected"
            } else -> {
            actionMode.title = "${selectedRecipe.size} items Selected"

        }
        }

    }


    /**
     * This method will be called from Fragment to update/ Set data
     */
    fun setData(newData: List<FavoriteRecipeEntity>) {
        val recipesDiffUtil = RecipeDiffUtil(favoriteRecipeEntity, newData)
        val diffUtil = DiffUtil.calculateDiff(recipesDiffUtil)
        favoriteRecipeEntity = newData

        diffUtil.dispatchUpdatesTo(this)
    }


    /**
     *  below four are contextual action mode call backs
     */
    override fun onCreateActionMode(actionMode: ActionMode?, menuItem: Menu?): Boolean {
        this.actionMode = actionMode!!
        actionMode?.menuInflater?.inflate(R.menu.favorite_recipe_contextual_mode, menuItem)
        showStatusBar(R.color.contextualStatusBardColor)

        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menuItem: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menuItem: MenuItem?): Boolean {
        if(menuItem?.itemId == R.id.favorite_recipe_delete_menu){
            selectedRecipe.forEach{recipe ->
                mainViewModel.deleteFavoriteRecipe(recipe)
            }
            showSnackBar("${selectedRecipe.size} Recipes removed")
            multiSelection = false
            selectedRecipe.clear()
            actionMode?.finish()

        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolderList.forEach() { holder ->
            changeRecipeStyle(holder, R.color.card_background_color, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipe.clear()
        showStatusBar(R.color.statusBarColor)
    }


    fun showStatusBar(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    private fun showSnackBar(message :String){
        Snackbar.make(rootView,message,Snackbar.LENGTH_SHORT).setAction("Okay",{}).show()
    }

    fun clearContextualActionMode() {
        if(this::actionMode.isInitialized) {
            actionMode.finish()
        }
    }
}