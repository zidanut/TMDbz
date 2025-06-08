package com.example.saz_ppb_prak7.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.saz_ppb_prak7.data.api.RetrofitClient;
import com.example.saz_ppb_prak7.data.api.TMDbApiService;
import com.example.saz_ppb_prak7.data.entity.Movie;
import com.example.saz_ppb_prak7.utils.NotificationHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private final TMDbApiService apiService;
    private final MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();
    private final NotificationHelper notificationHelper;

    public MovieRepository(Context context) {
        apiService = RetrofitClient.getApiService();
        notificationHelper = new NotificationHelper(context);
    }


    public LiveData<List<Movie>> getPopularMovies() {
        loadPopularMovies();
        return moviesLiveData;
    }

    public void loadPopularMovies() {
        apiService.getPopularMovies(TMDbApiService.API_KEY, "en-US", 1)
                .enqueue(new Callback<TMDbApiService.MovieResponse>() {
                    @Override
                    public void onResponse(Call<TMDbApiService.MovieResponse> call, Response<TMDbApiService.MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Movie> newMovies = response.body().getResults();
                            moviesLiveData.postValue(newMovies);

                            // Kirim notifikasi film baru
                            notificationHelper.sendNotification(
                                    1001,
                                    "Film Baru Tersedia",
                                    "Ada " + newMovies.size() + " film populer terbaru yang bisa kamu tonton!"
                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<TMDbApiService.MovieResponse> call, Throwable t) {
                        moviesLiveData.postValue(null);
                    }
                });
    }
}
