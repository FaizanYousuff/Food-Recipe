package com.mfy.recipes.ui.fragments.favorite

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mfy.recipes.MainViewModel
import com.mfy.recipes.R
import com.mfy.recipes.adpaters.FavoriteRecipeAdapter
import com.mfy.recipes.databinding.FragmentFavoriteRecipesBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModels()

    private val favoriteRecipeAdapter : FavoriteRecipeAdapter by lazy { FavoriteRecipeAdapter(requireActivity(),mainViewModel) }

    private var _binding : FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      _binding =  FragmentFavoriteRecipesBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.adapter = favoriteRecipeAdapter

        setupRecyclerView(binding.favoritesRecipeRecyclerView)

        // removed as we are observing from binding adapter
        /*mainViewModel.favoriteRecipeFromDatabase.observe(viewLifecycleOwner,{
            favoritesEntity -> favoriteRecipeAdapter.setData(favoritesEntity)

        })*/
        setHasOptionsMenu(true)
       return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipe_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.delete_all_favorite_recipe){
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar("All Recipes Deleted")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = favoriteRecipeAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        favoriteRecipeAdapter.clearContextualActionMode()
    }

    private fun showSnackBar(message :String){
        Snackbar.make(binding.root,message, Snackbar.LENGTH_SHORT).setAction("Okay",{}).show()
    }

}