package com.example.saz_ppb_prak7.data.api;

import com.example.saz_ppb_prak7.data.entity.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDbApiService {

    String API_KEY = "5d1044a2bcf9c96a3bb3e05b7e013ffc";

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    class MovieResponse {
        @SerializedName("results")
        private List<Movie> results;

        public List<Movie> getResults() {
            return results;
        }

        public void setResults(List<Movie> results) {
            this.results = results;
        }
    }
}
