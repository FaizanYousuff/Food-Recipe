package com.mfy.recipes.ui.fragments.recipe

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfy.recipes.MainViewModel
import com.mfy.recipes.R
import com.mfy.recipes.adpaters.RecipeAdapter
import com.mfy.recipes.databinding.FragmentRecipesBinding
import com.mfy.recipes.ui.viewModels.RecipeViewModel
import com.mfy.recipes.util.NetworkListener
import com.mfy.recipes.util.NetworkResult
import com.mfy.recipes.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

    private  val TAG = "RecipesFragment"

    private val  args by navArgs<RecipesFragmentArgs>()
    private lateinit var networkListener: NetworkListener


    private var _binding : FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel: RecipeViewModel
    private val adapter by lazy { RecipeAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        setupRecyclerView()
        readDatabase()
        setHasOptionsMenu(true)

        recipeViewModel.readBackOnline.observe(viewLifecycleOwner,{
            recipeViewModel.backOnline = it
        })
        binding.recipesFab.setOnClickListener{
            if(recipeViewModel.networkStatus){
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipeViewModel.showNetworkStatus()
            }
        }
        lifecycleScope.launch(){
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect{status ->
                    recipeViewModel.networkStatus = status
                    recipeViewModel.showNetworkStatus()

                }
        }
        return binding.root
    }
    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmer()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu,menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)


    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    private fun readDatabase() {
        lifecycleScope.launch(){
            mainViewModel.recipesFromDatabase.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d(TAG, "database Called")
                    adapter.setData(database[0].foodRecipe)
                    hideShimmer()
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        Log.d(TAG, "requestApiData")
        mainViewModel.getRecipes(recipeViewModel.getApiQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmer()
                    response.data?.let { adapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    hideShimmer()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }
                is NetworkResult.Loading -> {
                    showShimmer()
                }
            }
        }

    }


    private fun searchApiData(query: String) {
        Log.d(TAG, "requestApiData")
        mainViewModel.searchRecipe(recipeViewModel.getSearchApiQueries(query))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmer()
                    response.data?.let { adapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    hideShimmer()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }
                is NetworkResult.Loading -> {
                    showShimmer()
                }
            }
        }

    }

    private fun loadDataFromCache(){
        lifecycleScope.launch(){
            mainViewModel.recipesFromDatabase.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    adapter.setData(database[0].foodRecipe)
                }

            }
        }
    }



   private fun showShimmer() {
       binding.recyclerView.showShimmer()
    }

    private fun hideShimmer() {
        binding.recyclerView.hideShimmer()
    }



}