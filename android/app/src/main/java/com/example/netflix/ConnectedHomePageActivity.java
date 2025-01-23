package com.example.netflix;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix.adapters.CategoryAdapter;
import com.example.netflix.viewmodels.MovieViewModel;

public class ConnectedHomePageActivity extends AppCompatActivity {

    private static final String TAG = "ConnectedHomePage";
    private RecyclerView recyclerView;
    private EditText searchBar;
    private DrawerLayout drawerLayout;
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_home_page);

        // Setup DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // Setup Hamburger Icon
        ImageView hamburgerIcon = findViewById(R.id.hamburger_icon);
        hamburgerIcon.setOnClickListener(v -> {
            if (drawerLayout != null) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Setup Search Bar
        searchBar = findViewById(R.id.search_bar);

        // Add a TextWatcher for real-time search
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Trigger search when text length is more than 2 characters
                String query = s.toString().trim();
                if (s.length() > 2) {
                    Log.d(TAG, "Real-time search query: " + query);
                    fetchSearchedMovies(query);
                } else if (query.isEmpty()) {
                    Log.d(TAG, "Search bar cleared. Returning to default movies.");
                    observeMoviesByCategory();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // Setup RecyclerView
        recyclerView = findViewById(R.id.recycler_view_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Observe Default Movies
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

    private void fetchSearchedMovies(String query) {
        movieViewModel.searchMovies(query).observe(this, categorizedMovies -> {
            if (categorizedMovies != null && !categorizedMovies.isEmpty()) {
                Log.d(TAG, "Search results fetched successfully: " + categorizedMovies);
                CategoryAdapter categoryAdapter = new CategoryAdapter(this, categorizedMovies);
                recyclerView.setAdapter(categoryAdapter);
            } else {
                Log.d(TAG, "No search results found.");
            }
        });
    }

}
