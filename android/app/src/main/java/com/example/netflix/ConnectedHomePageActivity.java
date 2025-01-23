package com.example.netflix;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix.adapters.CategoryAdapter;
import com.example.netflix.viewmodels.MovieViewModel;
import com.google.android.material.navigation.NavigationView;

import com.example.netflix.dao.TokenDao;
import com.example.netflix.database.AppDatabase;

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

        // get app database instance
        AppDatabase database = AppDatabase.getInstance(this);
        TokenDao tokenDao = database.tokenDao();

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


        // Setup NavigationView
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Initialize ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Setup Navigation Item Selection
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                tokenDao.clearTokens();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else if(item.getItemId() == R.id.nav_categories) {
                Log.d(TAG, "Fetching all movies and categories.");
                getAllMoviesAndCategories();
            } else if(item.getItemId() == R.id.nav_home) {
                Log.d(TAG, "Fetching default movies by category.");
                observeMoviesByCategory();
            } else if(item.getItemId() == R.id.nav_management) {
                Intent intent = new Intent(this, ManagementActivity.class);
                startActivity(intent);
            }
            return true;
        });

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

            // Close the navigation drawer if open
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
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

    private void getAllMoviesAndCategories() {
        movieViewModel.getAllMovies().observe(this, categorizedMovies -> {
            if (categorizedMovies != null && !categorizedMovies.isEmpty()) {
                Log.d(TAG, "Movies fetched successfully: " + categorizedMovies);
                CategoryAdapter categoryAdapter = new CategoryAdapter(this, categorizedMovies);
                recyclerView.setAdapter(categoryAdapter);
            } else {
                Log.d(TAG, "No movies available to display.");
            }

            // Close the navigation drawer if open
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

}
