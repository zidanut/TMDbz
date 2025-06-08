package com.example.saz_ppb_prak7.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.saz_ppb_prak7.data.entity.Movie;
import com.example.saz_ppb_prak7.data.repository.MovieRepository;
import com.example.saz_ppb_prak7.data.repository.FavoriteMovieRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private final MovieRepository movieRepository;
    private final FavoriteMovieRepository favoriteMovieRepository;
    private LiveData<List<Movie>> movies;

    public MovieViewModel(MovieRepository movieRepository, FavoriteMovieRepository favoriteMovieRepository) {
        this.movieRepository = movieRepository;
        this.favoriteMovieRepository = favoriteMovieRepository;
        this.movies = movieRepository.getPopularMovies();
    }

    public void loadMovies() {
        movieRepository.loadPopularMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public FavoriteMovieRepository getFavoriteMovieRepository() {
        return favoriteMovieRepository;
    }
}
