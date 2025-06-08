package com.example.saz_ppb_prak7.presentation.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.saz_ppb_prak7.data.repository.MovieRepository;
import com.example.saz_ppb_prak7.data.repository.FavoriteMovieRepository;

public class MovieViewModelFactory implements ViewModelProvider.Factory {

    private final MovieRepository movieRepository;
    private final FavoriteMovieRepository favoriteMovieRepository;

    // Constructor menerima kedua repository
    public MovieViewModelFactory(MovieRepository movieRepository, FavoriteMovieRepository favoriteMovieRepository) {
        this.movieRepository = movieRepository;
        this.favoriteMovieRepository = favoriteMovieRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieViewModel(movieRepository, favoriteMovieRepository);  // Pass kedua repository
    }
}
