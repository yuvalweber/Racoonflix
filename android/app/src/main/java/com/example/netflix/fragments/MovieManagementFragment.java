package com.example.netflix.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.netflix.R;
import com.example.netflix.database.AppDatabase;
import com.example.netflix.models.Category;
import com.example.netflix.models.Movie;
import com.example.netflix.viewmodels.CategoryViewModel;
import com.example.netflix.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieManagementFragment extends Fragment {

    private static final String ARG_ACTION = "action";
    private static final String TAG = "MovieManagementFragment";

    private String action;
    private MovieViewModel movieViewModel;

    private CategoryViewModel categoryViewModel;

    public static MovieManagementFragment newInstance(String action) {
        MovieManagementFragment fragment = new MovieManagementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACTION, action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            action = getArguments().getString(ARG_ACTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_management, container, false);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Initialize fields
        EditText movieTitle = view.findViewById(R.id.edit_movie_title);
        EditText newMovieTitle = view.findViewById(R.id.edit_new_movie_title);
        EditText movieCategory = view.findViewById(R.id.edit_movie_category);
        EditText movieYear = view.findViewById(R.id.edit_movie_year);
        EditText movieDirector = view.findViewById(R.id.edit_movie_director);
        EditText movieDuration = view.findViewById(R.id.edit_movie_duration);
        EditText movieImage = view.findViewById(R.id.edit_movie_image);
        EditText movieTrailer = view.findViewById(R.id.edit_movie_trailer);
        Button submitButton = view.findViewById(R.id.button_submit);

        // Configure Submit Button Action
        submitButton.setOnClickListener(v -> {
            String movieNameInput = movieTitle.getText().toString().trim();
            String newMovieNameInput = newMovieTitle.getText().toString().trim();

            Movie movie = new Movie();
            if (newMovieNameInput.isEmpty()) {
                movie.setTitle(movieNameInput);
            } else {
                movie.setTitle(newMovieNameInput);
            }
            movie.setYear(movieYear.getText().toString().isEmpty() ? 0 : Integer.parseInt(movieYear.getText().toString().trim()));
            movie.setDirector(movieDirector.getText().toString().trim());
            movie.setDuration(movieDuration.getText().toString().isEmpty() ? 0 : Integer.parseInt(movieDuration.getText().toString().trim()));
            movie.setImage(movieImage.getText().toString().trim());
            movie.setTrailer(movieTrailer.getText().toString().trim());
            if (!movieCategory.getText().toString().isEmpty()) {
                String[] categories = movieCategory.getText().toString().split(",");
                // remove space from each category
                for (int i = 0; i < categories.length; i++) {
                    categories[i] = categories[i].trim();
                }
                List<String> categoryNames = List.of(categories);
                movie.setCategory(categoryNames);
                List<String> categoryIds = new ArrayList<>();
                categoryViewModel.fetchCategoriesIdByName(categories).forEach(category -> {
                    categoryIds.add(category.getId());
                });

                Log.d(TAG, "Category Ids: " + categoryIds);

                if (categoryIds.size() != categoryNames.size()) {
                    Log.e(TAG, "Failed to find all categories: " + categoryNames);
                    Toast.makeText(getContext(), "Error: Failed to find all categories: " + categoryNames, Toast.LENGTH_SHORT).show();
                    return;
                }

                movie.setCategory(categoryIds);

            } else {
                movie.setCategory(List.of());
            }

            switch (action) {
                case "Create":
                    movieViewModel.createMovie(movie, new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "Movie created successfully.");
                                Toast.makeText(getContext(), "Movie created successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e(TAG, "Failed to create movie: " + t.getMessage());
                            Toast.makeText(getContext(), "Failed to create movie: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case "Update":
                    if (movieNameInput.isEmpty()) {
                        Toast.makeText(getContext(), "Movie name is required for update.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    movieViewModel.fetchMovieIdByName(movieNameInput, new Callback<List<Movie>>() {
                        @Override
                        public void onResponse(Call<List<Movie>> call, Response<List<Movie>>response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "Movie updated successfully.");
                                String movieId = response.body().get(0).getId();
                                movieViewModel.updateMovie(movieId, movie, new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Log.d(TAG, "Movie updated successfully.");
                                            Toast.makeText(getContext(), "Movie updated successfully.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e(TAG, "Failed to update movie: " + t.getMessage());
                                        Toast.makeText(getContext(), "Failed to update movie: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(getContext(), "Movie updated successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Movie>> call, Throwable t) {
                            Log.e(TAG, "Failed to update movie: " + t.getMessage());
                            Toast.makeText(getContext(), "Failed to update movie: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;

                case "Delete":
                    if (movieNameInput.isEmpty()) {
                        Toast.makeText(getContext(), "Movie name is required for delete.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    movieViewModel.fetchMovieIdByName(movieNameInput, new Callback<List<Movie>>() {
                        @Override
                        public void onResponse(Call<List<Movie>> call, Response<List<Movie>>response) {
                            if (response.isSuccessful()) {
                                String movieId = response.body().get(0).getId();
                                movieViewModel.deleteMovie(movieId, new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Log.d(TAG, "Movie Deleted successfully.");
                                            Toast.makeText(getContext(), "Movie Deleted successfully.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e(TAG, "Failed to Delete movie: " + t.getMessage());
                                        Toast.makeText(getContext(), "Failed to Delete movie: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(getContext(), "Movie Deleted successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Movie>> call, Throwable t) {
                            Log.e(TAG, "Failed to Delete movie: " + t.getMessage());
                            Toast.makeText(getContext(), "Failed to Delete movie: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;

                default:
                    Log.e(TAG, "Invalid action: " + action);
            }
        });

        // Show/hide fields based on action
        if (action.equals("Delete")) {
            movieCategory.setVisibility(View.GONE);
            movieYear.setVisibility(View.GONE);
            movieDirector.setVisibility(View.GONE);
            movieDuration.setVisibility(View.GONE);
            movieImage.setVisibility(View.GONE);
            movieTrailer.setVisibility(View.GONE);
        } else if (action.equals("Create")) {
            newMovieTitle.setVisibility(View.GONE);
        }

        // because we changed info about movies lets invalidate the cache
        AppDatabase appDatabase = AppDatabase.getInstance(getContext());
        Thread thread = new Thread(() -> {appDatabase.movieDao().clearMovies();});
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return view;
    }
}
