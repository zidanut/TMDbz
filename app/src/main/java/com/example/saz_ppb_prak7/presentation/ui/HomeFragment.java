package com.example.saz_ppb_prak7.presentation.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saz_ppb_prak7.R;
import com.example.saz_ppb_prak7.presentation.adapter.MovieAdapter;
import com.example.saz_ppb_prak7.presentation.viewmodel.MovieViewModel;
import com.example.saz_ppb_prak7.presentation.viewmodel.MovieViewModelFactory;
import com.example.saz_ppb_prak7.data.repository.MovieRepository;
import com.example.saz_ppb_prak7.data.repository.FavoriteMovieRepository;
import com.example.saz_ppb_prak7.utils.NotificationHelper;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final float SHAKE_THRESHOLD = 9.0f;
    private static final int SHAKE_WAIT_TIME_MS = 500;
    private long mShakeTimestamp;
    private MovieViewModel movieViewModel;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);
            Log.d("ShakeDetector", "gForce = " + gForce);
            if (gForce > SHAKE_THRESHOLD) {
                final long now = System.currentTimeMillis();
                if (mShakeTimestamp + SHAKE_WAIT_TIME_MS > now) {
                    return;
                }
                mShakeTimestamp = now;

                Log.d("ShakeDetector", "Shake detected!");
                onShakeDetected();
            }
        }
    }

    private void onShakeDetected() {
        Toast.makeText(requireContext(), "Shake detected! Refreshing movie list...", Toast.LENGTH_SHORT).show();
        if (movieViewModel != null) {
            movieViewModel.loadMovies();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewMovies);
        progressBar = view.findViewById(R.id.progressBar);
        MovieRepository movieRepository = new MovieRepository(requireContext());
        FavoriteMovieRepository favoriteMovieRepository = new FavoriteMovieRepository(requireContext());
        NotificationHelper notificationHelper = new NotificationHelper(requireContext());
        MovieViewModelFactory factory = new MovieViewModelFactory(movieRepository, favoriteMovieRepository);
        movieViewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);
        movieAdapter = new MovieAdapter(new ArrayList<>(), favoriteMovieRepository, notificationHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(movieAdapter);

        progressBar.setVisibility(View.VISIBLE);
        movieViewModel.getMovies().observe(getViewLifecycleOwner(), movies -> {
            progressBar.setVisibility(View.GONE);
            if (movies != null) {
                movieAdapter.setMovies(movies);
            } else {
            }
        });

        return view;
    }
}
