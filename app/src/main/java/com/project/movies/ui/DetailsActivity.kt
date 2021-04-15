package com.project.movies.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.project.movies.R
import com.project.movies.adapters.PagerAdapter
import com.project.movies.data.database.entities.FavoritesEntity
import com.project.movies.ui.fragments.overview.OverviewFragment
import com.project.movies.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var movieSaved = false
    private var savedMovieId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")

        val resultBundle = Bundle()
        resultBundle.putParcelable("movieBundle", args.result)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        checkSavedMovies(menuItem!!)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }else if (item.itemId == R.id.save_to_favorites_menu && !movieSaved){
            saveToFavorites(item)
        }else if (item.itemId == R.id.save_to_favorites_menu && movieSaved){
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedMovies(menuItem: MenuItem) {
        mainViewModel.readFavoriteMovies.observe(this, { favoriteEntity ->
            try {
                for (savedMovie in favoriteEntity){
                    if (savedMovie.result.id == args.result.id){
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedMovieId = savedMovie.id
                        movieSaved = true
                    }else{
                        changeMenuItemColor(menuItem, R.color.white)
                    }
                }
            }catch (e: Exception){
                Log.d("DetailsActivity", e.message.toString())
            }

        })
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(
                0,
                args.result
        )
        mainViewModel.insertFavoriteMovie(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Movie saved.")
        movieSaved = true
    }

    private fun removeFromFavorites(item: MenuItem){
        val favoritesEntity =
                FavoritesEntity(
                    savedMovieId,
                    args.result
                )
        mainViewModel.deleteFavoriteMovie(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favorites.")
        movieSaved = false

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
                detailsLayout,
                message,
                Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
                .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }
}