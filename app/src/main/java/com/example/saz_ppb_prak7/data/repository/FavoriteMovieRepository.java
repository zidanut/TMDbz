package com.example.saz_ppb_prak7.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.saz_ppb_prak7.data.database.MovieDatabaseHelper;
import com.example.saz_ppb_prak7.data.entity.Movie;
import com.example.saz_ppb_prak7.data.entity.MovieFavorite;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMovieRepository {

    private final SQLiteDatabase database;
    private final MovieDatabaseHelper dbHelper;

    public FavoriteMovieRepository(Context context) {
        dbHelper = new MovieDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();  // Membuka database untuk operasi tulis
    }

    // Menambahkan film ke database
    public void insert(MovieFavorite movieFavorite) {
        ContentValues values = new ContentValues();
        values.put(MovieDatabaseHelper.COLUMN_TITLE, movieFavorite.getTitle());
        values.put(MovieDatabaseHelper.COLUMN_OVERVIEW, movieFavorite.getOverview());
        values.put(MovieDatabaseHelper.COLUMN_POSTER_PATH, movieFavorite.getPoster_path());

        long rowId = database.insert(MovieDatabaseHelper.TABLE_FAVORITE_MOVIES, null, values);
        if (rowId == -1) {
        }
    }

    public List<Movie> getAllFavoriteMovies() {
        List<Movie> favoriteMovies = new ArrayList<>();
        Cursor cursor = database.query(
                MovieDatabaseHelper.TABLE_FAVORITE_MOVIES,
                new String[]{MovieDatabaseHelper.COLUMN_ID, MovieDatabaseHelper.COLUMN_TITLE, MovieDatabaseHelper.COLUMN_OVERVIEW, MovieDatabaseHelper.COLUMN_POSTER_PATH},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MovieDatabaseHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.COLUMN_TITLE));
                String overview = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.COLUMN_OVERVIEW));
                String posterPath = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.COLUMN_POSTER_PATH));

                Movie movie = new Movie();
                movie.setId(id);
                movie.setTitle(title);
                movie.setOverview(overview);
                movie.setPoster_path(posterPath);
                favoriteMovies.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return favoriteMovies;
    }


    // Menghapus film favorit berdasarkan ID
    public void deleteMovieById(int movieId) {
        int rowsDeleted = database.delete(
                MovieDatabaseHelper.TABLE_FAVORITE_MOVIES,
                MovieDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(movieId)});
        if (rowsDeleted == 0) {
            // Handle error if deletion fails (optional)
        }
    }

    // Menghapus semua film favorit (opsional)
    public void deleteAllMovies() {
        database.delete(MovieDatabaseHelper.TABLE_FAVORITE_MOVIES, null, null);
    }

    public MovieFavorite getMovieById(int movieId) {
        Cursor cursor = database.query(
                MovieDatabaseHelper.TABLE_FAVORITE_MOVIES,
                new String[]{MovieDatabaseHelper.COLUMN_ID, MovieDatabaseHelper.COLUMN_TITLE, MovieDatabaseHelper.COLUMN_OVERVIEW, MovieDatabaseHelper.COLUMN_POSTER_PATH},
                MovieDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(movieId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MovieDatabaseHelper.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.COLUMN_TITLE));
            String overview = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.COLUMN_OVERVIEW));
            String posterPath = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.COLUMN_POSTER_PATH));
            cursor.close();
            return new MovieFavorite(title, overview, posterPath);
        }
        return null;
    }
}