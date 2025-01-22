package com.example.netflix;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix.adapters.CategoryAdapter;
import com.example.netflix.models.Movie;
import com.example.netflix.viewmodels.MovieViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Map;

public class ConnectedHomePageActivity extends AppCompatActivity {

    private static final String TAG = "ConnectedHomePage";
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_home_page);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup DrawerLayout and Toggle
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Setup NavigationView
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Observe LiveData from ViewModel
        observeMoviesByCategory();
    }

    private void observeMoviesByCategory() {
        movieViewModel.getMoviesByCategory().observe(this, categorizedMovies -> {
            if (categorizedMovies != null && !categorizedMovies.isEmpty()) {
                Log.d(TAG, "Movies fetched successfully: " + categorizedMovies);
                CategoryAdapter categoryAdapter = new CategoryAdapter(this, categorizedMovies);
                recyclerView.setAdapter(categoryAdapter);
            } else {
                Log.d(TAG, "No movies available to display.");
            }
        });
    }
}
