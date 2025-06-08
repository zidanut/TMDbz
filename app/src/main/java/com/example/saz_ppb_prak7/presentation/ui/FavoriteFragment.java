package com.example.saz_ppb_prak7.presentation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saz_ppb_prak7.R;
import com.example.saz_ppb_prak7.data.entity.Movie;
import com.example.saz_ppb_prak7.presentation.adapter.MovieAdapter;
import com.example.saz_ppb_prak7.data.repository.FavoriteMovieRepository;
import com.example.saz_ppb_prak7.utils.NotificationHelper;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private MovieAdapter movieAdapter;
    private FavoriteMovieRepository favoriteMovieRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorit, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFavoriteMovies);

        favoriteMovieRepository = new FavoriteMovieRepository(requireContext());
        NotificationHelper notificationHelper = new NotificationHelper(requireContext());

        movieAdapter = new MovieAdapter(new ArrayList<>(), favoriteMovieRepository, notificationHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(movieAdapter);

        new Thread(() -> {
            List<Movie> favoriteMovies = favoriteMovieRepository.getAllFavoriteMovies();
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> movieAdapter.setMovies(favoriteMovies));
        }).start();

        return view;
    }
}

