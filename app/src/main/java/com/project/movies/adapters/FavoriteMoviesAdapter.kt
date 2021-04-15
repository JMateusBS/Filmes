package com.project.movies.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.project.movies.R
import com.project.movies.data.database.entities.FavoritesEntity
import com.project.movies.databinding.FavoriteMoviesRowLayoutBinding
import com.project.movies.ui.fragments.favorites.FavoriteMoviesFragmentDirections
import com.project.movies.util.MoviesDiffUtil
import com.project.movies.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.favorite_movies_row_layout.view.*

class FavoriteMoviesAdapter(
        private val requiredActivity: FragmentActivity,
        private val mainViewModel: MainViewModel
): RecyclerView.Adapter<FavoriteMoviesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var multiSelection = false

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var selectedMovies = arrayListOf<FavoritesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()
    private var favoriteMovies = emptyList<FavoritesEntity>()

    class MyViewHolder(private val binding: FavoriteMoviesRowLayoutBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteMoviesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView

        val currentMovie = favoriteMovies[position]
        holder.bind(currentMovie)

        /**Single Click listener**/
        holder.itemView.favoriteMoviesRowLayout.setOnClickListener {
            if (multiSelection){
                applySelection(holder, currentMovie)
            }else {
                val action =
                        FavoriteMoviesFragmentDirections.actionFavoriteMoviesFragmentToDetailsActivity(
                                currentMovie.result
                        )
                holder.itemView.findNavController().navigate(action)
            }

        }

        /**Long Click listener**/
        holder.itemView.favoriteMoviesRowLayout.setOnLongClickListener {
            if (!multiSelection){
                multiSelection = true
                requiredActivity.startActionMode(this)
                applySelection(holder, currentMovie)
                true
            }else{
                multiSelection = false
                false
            }
        }
    }

    private fun applySelection(holder: MyViewHolder, currentMovie: FavoritesEntity){
        if (selectedMovies.contains(currentMovie)){
            selectedMovies.remove(currentMovie)
            changeMovieStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        }else {
            selectedMovies.add(currentMovie)
            changeMovieStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeMovieStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int){
        holder.itemView.favoriteMoviesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requiredActivity, backgroundColor)
        )
        holder.itemView.favorite_row_cardView.strokeColor =
            ContextCompat.getColor(requiredActivity, strokeColor)
    }

    private fun applyActionModeTitle(){
        when(selectedMovies.size) {
            0 -> {
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "${selectedMovies.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedMovies.size} items selected"
            }
        }
    }

    override fun getItemCount(): Int {
        return favoriteMovies.size
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_favorite_movie_menu){
            selectedMovies.forEach {
                mainViewModel.deleteFavoriteMovie(it)
            }
            showSnackBar("${selectedMovies.size} Recipe/s removed.")

            multiSelection = false
            selectedMovies.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeMovieStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedMovies.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int){
        requiredActivity.window.statusBarColor =
                ContextCompat.getColor(requiredActivity, color)
    }

    fun setData(newFavoriteMovies: List<FavoritesEntity>){
        val favoriteMoviesDiffUtil =
                MoviesDiffUtil(favoriteMovies, newFavoriteMovies)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteMoviesDiffUtil)
        favoriteMovies = newFavoriteMovies
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String){
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){

        }.show()
    }

    fun clearContextualActionMode(){
        if (this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }

}