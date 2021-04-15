package com.project.movies.ui.fragments.movies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.movies.R
import com.project.movies.viewmodels.MainViewModel
import com.project.movies.adapters.MoviesAdapter
import com.project.movies.databinding.FragmentMoviesBinding
import com.project.movies.util.NetworkResult
import com.project.movies.util.observeOnce
import com.project.movies.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var moviesViewModel: MoviesViewModel
    private val mAdapter by lazy {MoviesAdapter()}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        moviesViewModel = ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        setupRecyclerView()
        requestApiData()

//        binding.moviesFloatBtn.setOnClickListener{
//            findNavController().navigate(R.id.action_moviesFragment_to_moviesBottomSheet)
//        }

        return binding.root
    }

    private fun setupRecyclerView(){
        binding.moviesRecycle.adapter = mAdapter
        binding.moviesRecycle.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun readDatabase() {
       lifecycleScope.launch{
           mainViewModel.readMovies.observeOnce(viewLifecycleOwner,{database ->
               if (database.isNotEmpty()){
                   Log.d("MoviesFragment", "readDatabase called!")
                   mAdapter.setData(database[0].movies)
                   hideShimmerEffects()
               } else {
                   requestApiData()
               }
           })
       }
    }

    private fun requestApiData(){
        Log.d("MoviesFragment", "requestApiData called!")
        mainViewModel.getMovies(moviesViewModel.applyQueries())
        mainViewModel.moviesResponse.observe(viewLifecycleOwner,{response ->
                when(response){
                    is NetworkResult.Success -> {
                        hideShimmerEffects()
                        response.data?.let { mAdapter.setData(it) }
                    }
                    is NetworkResult.Error -> {
                        hideShimmerEffects()
                        loadDataFromCache()
                        Toast.makeText(
                                requireContext(),
                                response.message.toString(),
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkResult.Loading -> {
                        showShimmerEffect()
                    }
                }
        })

    }

    private fun loadDataFromCache(){
        mainViewModel.readMovies.observe(viewLifecycleOwner,{database ->
            if (database.isNotEmpty()){
                mAdapter.setData(database[0].movies)
            }
        })
    }

    private fun showShimmerEffect(){
        binding.moviesRecycle.showShimmer()
    }

    private fun hideShimmerEffects(){
        binding.moviesRecycle.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}