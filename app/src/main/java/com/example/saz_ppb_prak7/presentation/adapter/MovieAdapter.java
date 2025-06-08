package com.example.saz_ppb_prak7.presentation.adapter;

import com.example.saz_ppb_prak7.data.entity.Movie;
import com.example.saz_ppb_prak7.data.entity.MovieFavorite;
import com.example.saz_ppb_prak7.data.repository.FavoriteMovieRepository;
import com.example.saz_ppb_prak7.utils.NotificationHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saz_ppb_prak7.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private FavoriteMovieRepository favoriteMovieRepository;
    private NotificationHelper notificationHelper;

    public MovieAdapter(List<Movie> movies, FavoriteMovieRepository repository, NotificationHelper notificationHelper) {
        this.movieList = movies;
        this.favoriteMovieRepository = repository;
        this.notificationHelper = notificationHelper;
    }

    public void setMovies(List<Movie> movies) {
        this.movieList = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvDescription.setText(movie.getOverview());

        String imageUrl = "https://image.tmdb.org/t/p/original" + movie.getPoster_path();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.ivPoster);

        boolean isFavorite = isMovieFavorite(movie);
        if (isFavorite) {
            holder.ivFavorite.setImageResource(R.drawable.ic_star_filled);
        } else {
            holder.ivFavorite.setImageResource(R.drawable.ic_star_border);
        }

        holder.ivFavorite.setOnClickListener(v -> {
            if (!isFavorite) {
                favoriteMovieRepository.insert(new MovieFavorite(movie.getTitle(), movie.getOverview(), movie.getPoster_path()));
                holder.ivFavorite.setImageResource(R.drawable.ic_star_filled);

                // Panggil notifikasi
                notificationHelper.sendNotification(
                        (int) System.currentTimeMillis(),
                        "Favorit Ditambahkan",
                        "Film \"" + movie.getTitle() + "\" berhasil ditambahkan ke favorit!"
                );
            } else {
                favoriteMovieRepository.deleteMovieById(movie.getId());
                holder.ivFavorite.setImageResource(R.drawable.ic_star_border);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    private boolean isMovieFavorite(Movie movie) {
        List<Movie> favoriteMovies = favoriteMovieRepository.getAllFavoriteMovies();
        for (Movie favoriteMovie : favoriteMovies) {
            if (favoriteMovie.getTitle().equals(movie.getTitle())) {
                return true;
            }
        }
        return false;
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;
        ImageView ivPoster, ivFavorite;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
        }
    }
}
