package com.guerrero.upcomingmovies.movies.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.guerrero.upcomingmovies.R
import com.guerrero.upcomingmovies.databinding.FragmentMovieDetailsBinding
import com.guerrero.upcomingmovies.movies.MoviesViewModel
import com.guerrero.upcomingmovies.shared.IMAGES_BASE_URL
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding

    private val moviesViewModel: MoviesViewModel by sharedViewModel()

    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMovieDetails()
        moviesViewModel.requestMovie(args.movieId)
        setupBackNavigation()
    }

    private fun observeMovieDetails() {
        moviesViewModel.getMovieDetailsObservable().observe(viewLifecycleOwner, { movie ->
            loadPosterInToolbar(movie.posterPath)
            with(binding) {
                collapsingToolbarLayout.title = movie.title
                overview.text = movie.overview
                releaseDate.text = movie.releaseDate
                popularity.text = movie.popularity.toString()
            }

        })
    }

    private fun loadPosterInToolbar(posterPath: String) {
        Glide
            .with(requireContext())
            .load("$IMAGES_BASE_URL${posterPath}")
            .placeholder(R.drawable.ic_baseline_local_movies)
            .apply(
                RequestOptions.bitmapTransform(
                    VignetteFilterTransformation()
                )
            )
            .into(binding.toolbarBackground)
    }

    private fun setupBackNavigation() {
        binding.toolbar.setNavigationOnClickListener {
            it.findNavController().popBackStack()
        }
    }
}