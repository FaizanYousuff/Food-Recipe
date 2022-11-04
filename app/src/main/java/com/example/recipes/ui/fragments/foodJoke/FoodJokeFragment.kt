package com.example.recipes.ui.fragments.foodJoke

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.recipes.MainViewModel
import com.example.recipes.R
import com.example.recipes.databinding.FragmentFoodJokeBinding
import com.example.recipes.util.Constants.Companion.API_KEY
import com.example.recipes.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment()  {

    // using delete method to create viewmodels
    private val mainViewModel  by viewModels<MainViewModel>()

    private var _binding : FragmentFoodJokeBinding? = null
    private val binding get() = _binding!!

    private var foodJoke = "No Food Joke"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentFoodJokeBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        //setHasOptionsMenu(true)

        mainViewModel.getFoodJoke(API_KEY)
        mainViewModel.fooJokeResponse.observe(viewLifecycleOwner) { foodJokeResponse ->

            when(foodJokeResponse){
                is NetworkResult.Success ->{
                    binding.foodJokeTextView.text = foodJokeResponse.data?.text
                    if(foodJokeResponse.data != null){
                        foodJoke = foodJokeResponse.data.text

                    }
                }

                is NetworkResult.Error ->{
                    Toast.makeText(requireContext(),foodJokeResponse.message,Toast.LENGTH_SHORT).show()
                    loadDataFromCache()
                }
                is NetworkResult.Loading ->{

                }
            }

        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.share_food_joke_menu){
            val shareIntent  = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT,foodJoke )

            }
            startActivity(shareIntent)

        }

        return super.onOptionsItemSelected(item)
    }

    fun loadDataFromCache() {
        lifecycleScope.launch() {
            mainViewModel.readFoodJokes.observe(viewLifecycleOwner){ database ->
                if(!database.isNullOrEmpty()){
                    binding.foodJokeTextView.text = database[0].foodJoke.text
                    foodJoke = database[0].foodJoke.text
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}