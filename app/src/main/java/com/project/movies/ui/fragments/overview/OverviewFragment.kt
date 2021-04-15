package com.project.movies.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.project.movies.R
import com.project.movies.model.Result
import com.project.movies.util.Constants.Companion.IMAGE_URL
import kotlinx.android.synthetic.main.fragment_overview.view.*


class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable("movieBundle")
        val bundleImage = IMAGE_URL + myBundle?.posterPath


        view.main_imageView.load(bundleImage)
        view.title_textView.text = myBundle?.title
        view.vote_textView.text = myBundle?.voteAverage.toString()
        view.summary_textView.text = myBundle?.overview.toString()

        return view
    }


}