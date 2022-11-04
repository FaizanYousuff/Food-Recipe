package com.example.recipes.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.example.recipes.MainViewModel
import com.example.recipes.R
import com.example.recipes.adpaters.PagerAdapter
import com.example.recipes.data.database.entities.FavoriteRecipeEntity
import com.example.recipes.ui.details.fragments.IngredientsFragment
import com.example.recipes.ui.details.fragments.InstructionsFragment
import com.example.recipes.ui.details.fragments.OverviewFragment
import com.example.recipes.util.Constants.Companion.RESULT_BUNDLE_KEY
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val arg by navArgs<DetailsActivityArgs>()

    // Lazy initializing View model
    private val mainViewModel: MainViewModel by viewModels()

    private var recipesSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(details_tool_bar)
        details_tool_bar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("OverView")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()

        resultBundle.putParcelable(RESULT_BUNDLE_KEY, arg.result)


        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager

        )

        details_view_pager.adapter = adapter
        details_tab_layout.setupWithViewPager(details_view_pager)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorite_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.favoriteRecipeFromDatabase.observe(this) { favoritesEnity ->

            try {
                changeMenuItemColor(menuItem, R.color.white)
                for (savedRecipe in favoritesEnity) {
                    if (savedRecipe.result.id == arg.result.id) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipesSaved = true
                    } /*else {
                        changeMenuItemColor(menuItem, R.color.white)

                    }*/

                }

            } catch (e: java.lang.Exception) {
                Log.e("checkSavedRecipes", e.message.toString())
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorite_menu) {
            if (recipesSaved) {
                removeFromFavorites(item)
            } else {
                saveToFavorites(item)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun saveToFavorites(item: MenuItem) {
        val favoriteRecipeEntity = FavoriteRecipeEntity(
            0, arg.result
        )
        mainViewModel.insertFavoriteRecipes(favoriteRecipeEntity)

        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipes Saved")
        recipesSaved = true

    }

    private fun removeFromFavorites(menuItem: MenuItem) {
        val favoriteRecipeEntity = FavoriteRecipeEntity(
            savedRecipeId, arg.result
        )
        mainViewModel.deleteFavoriteRecipe(favoriteRecipeEntity)
        changeMenuItemColor(menuItem, R.color.white)
        showSnackBar("Removed from Favorites")
        recipesSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            details_layout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}.show()

    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }


}