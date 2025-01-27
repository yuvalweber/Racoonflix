package com.example.netflix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix.adapters.CategoryAdapter;
import com.example.netflix.api.UserApiService;
import com.example.netflix.models.Movie;
import com.example.netflix.network.RetrofitInstance;
import com.example.netflix.repository.UserRepository;
import com.example.netflix.viewmodels.MovieViewModel;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.AppCompatDelegate;
import android.net.Uri;
import android.media.MediaPlayer;

import com.example.netflix.dao.TokenDao;
import com.example.netflix.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectedHomePageActivity extends AppCompatActivity {

    private static final String TAG = "ConnectedHomePage";
    private RecyclerView recyclerView;
    private EditText searchBar;
    private DrawerLayout drawerLayout;

    private VideoView videoView;

    private FrameLayout movieFrameLayout;

    private UserRepository userRepository;
    private MovieViewModel movieViewModel;

    private static final String PREFS_NAME = "theme_prefs";
    private static final String PREF_DARK_MODE = "is_dark_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_home_page);

        // Apply the saved theme preference
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean(PREF_DARK_MODE, false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        // Setup DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // get app database instance
        AppDatabase database = AppDatabase.getInstance(this);
        UserApiService apiService = RetrofitInstance.getInstance().create(UserApiService.class);
        TokenDao tokenDao = database.tokenDao();
        userRepository = new UserRepository(apiService, tokenDao);

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
                    movieFrameLayout.setVisibility(FrameLayout.GONE);
                    videoView.pause();
                    fetchSearchedMovies(query);
                } else if (query.isEmpty()) {
                    Log.d(TAG, "Search bar cleared. Returning to default movies.");
                    movieFrameLayout.setVisibility(FrameLayout.VISIBLE);
                    videoView.start();
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

        // Fetch Admin Status and Update Menu
        userRepository.fetchTokenInfo(new UserRepository.UserCallback() {
            @Override
            public void onSuccess(Object data) {
                runOnUiThread(() -> {
                    boolean isAdmin = (boolean) ((Map<String, Object>) data).get("isAdmin");
                    MenuItem managementItem = navigationView.getMenu().findItem(R.id.nav_management);
                    managementItem.setVisible(isAdmin); // Show/Hide "Management" menu item
                });
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                Log.e(TAG, "Error fetching token info: " + errorMessage);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Failed to fetch token info", t);
            }
        });

        // Initialize ViewModel
        videoView = findViewById(R.id.video_view);
        movieFrameLayout = findViewById(R.id.random_movie_player_container);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        AtomicBoolean isCategoryEnabled = new AtomicBoolean(false);

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
                isCategoryEnabled.set(true);
                getAllMoviesAndCategories();
            } else if(item.getItemId() == R.id.nav_home) {
                Log.d(TAG, "Fetching default movies by category.");
                observeMoviesByCategory();
                fetchAndPlayRandomMovie();
            } else if(item.getItemId() == R.id.nav_management) {
                Intent intent = new Intent(this, ManagementActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.action_toggle_theme) {
                // Toggle the theme
                SharedPreferences prefs2 = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                boolean isDarkMode2 = prefs2.getBoolean(PREF_DARK_MODE, false);

                // Save the new theme preference
                SharedPreferences.Editor editor = prefs2.edit();
                editor.putBoolean(PREF_DARK_MODE, !isDarkMode2);
                editor.apply();

                // Apply the new theme
                AppCompatDelegate.setDefaultNightMode(
                        !isDarkMode2 ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
                );

                if (isCategoryEnabled.get()) {
                    Intent intent = new Intent(this, ConnectedHomePageActivity.class);
                    intent.putExtra("CATEGORY_NAVIGATION", true);
                    finish();
                    startActivity(intent);
                }
            }
            return true;
        });

        if (getIntent().getBooleanExtra("CATEGORY_NAVIGATION", false)) {
            Log.d(TAG, "Navigating to Categories.");
            getAllMoviesAndCategories();
        } else {
            Log.d(TAG, "Navigating to Home.");
            observeMoviesByCategory();
            fetchAndPlayRandomMovie();
        }
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
            movieFrameLayout.setVisibility(FrameLayout.GONE);
            videoView.pause();
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

    private void fetchAndPlayRandomMovie() {
        movieViewModel.getMoviesByCategory().observe(this, movies -> {
            movieFrameLayout.setVisibility(FrameLayout.VISIBLE);
            if (movies != null && !movies.isEmpty()) {
                // Select a random movie
                Movie randomMovie = getRandomMovie(movies);
                Log.d(TAG, "Random movie selected: " + randomMovie);
                if (randomMovie == null) {
                    Toast.makeText(this, "No movies available to play.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (randomMovie.getTrailer() != null) {
                    Log.d(TAG, "Playing random movie: " + randomMovie.getTrailer());
                    playVideo(randomMovie.getTrailer());
                }
            }
        });
    }

    private Movie getRandomMovie(Map<String, List<Movie>> categorizedMovies) {
        Random random = new Random();
        List<Movie> allMovies = new ArrayList<>();
        for (List<Movie> movieList : categorizedMovies.values()) {
            allMovies.addAll(movieList);
        }

        // Return a random movie from the flattened list
        if (!allMovies.isEmpty()) {
            return allMovies.get(random.nextInt(allMovies.size()));
        }
        return null;
    }

    private void playVideo(String videoUrl) {
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.setOnPreparedListener(MediaPlayer::start);
        videoView.setOnCompletionListener(mp -> {
            // Optionally restart or show a placeholder when video ends
            videoView.seekTo(0);
            videoView.start();
        });
    }



}
