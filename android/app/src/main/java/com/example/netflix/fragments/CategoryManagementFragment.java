package com.example.netflix.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.netflix.R;
import com.example.netflix.models.Category;
import com.example.netflix.models.Movie;
import com.example.netflix.viewmodels.CategoryViewModel;
import com.example.netflix.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryManagementFragment extends Fragment {

    private static final String ARG_ACTION = "action";
    private static final String TAG = "CategoryManagementFragment";

    private String action;
    private CategoryViewModel categoryViewModel;

    private MovieViewModel movieViewModel;

    public static CategoryManagementFragment newInstance(String action) {
        CategoryManagementFragment fragment = new CategoryManagementFragment();
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
        View view = inflater.inflate(R.layout.fragment_category_management, container, false);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);


        // Initialize fields
        EditText categoryName = view.findViewById(R.id.edit_category_name);
        EditText categoryMovies = view.findViewById(R.id.edit_category_movies);
        Switch promotedSwitch = view.findViewById(R.id.switch_promoted);
        Button submitButton = view.findViewById(R.id.button_submit);

        // Configure Submit Button Action
        submitButton.setOnClickListener(v -> {
            Category category = new Category();
            category.setName(categoryName.getText().toString().trim());
            if (!categoryMovies.getText().toString().isEmpty()) {
                String[] movies = categoryMovies.getText().toString().split(",");
                // remove leading and trailing whitespaces
                for (int i = 0; i < movies.length; i++) {
                    movies[i] = movies[i].trim();
                }
                List<String> movieNames = List.of(movies);
                category.setMovies(movieNames);
                List<String> movieIds = new ArrayList<>();

                movieViewModel.fetchMoviesIdByName(movies).forEach(movie -> {
                    movieIds.add(movie.getId());
                });

                if (movieIds.size() != movieNames.size()) {
                    Toast.makeText(getContext(), "Error: One or more movies not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "Movies: " + movieIds);

                category.setMovies(movieIds);

            } else {
                category.setMovies(List.of());
            }
            category.setPromoted(promotedSwitch.isChecked());

            // Execute the appropriate action
            switch (action) {
                case "Create":
                    Log.d(TAG, "Creating category: " + category);
                    categoryViewModel.createCategory(category, new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Category created successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getContext(), "Failed to create category: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;

                case "Update":
                    String categoryNameInput = categoryName.getText().toString().trim();
                    categoryViewModel.fetchCategoryIdByName(categoryNameInput, new Callback<List<Category>>() {
                        @Override
                        public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String categoryId = response.body().get(0).getId();
                                Log.d(TAG, "Updating category: " + category);
                                categoryViewModel.updateCategory(categoryId, category, new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(getContext(), "Category updated successfully.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(getContext(), "Failed to update category: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Category not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<List<Category>> call, Throwable t) {
                            Toast.makeText(getContext(), "Error finding category: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;

                case "Delete":
                    String categoryNameInputDelete = categoryName.getText().toString().trim();
                    categoryViewModel.fetchCategoryIdByName(categoryNameInputDelete, new Callback<List<Category>>() {
                        @Override
                        public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String categoryId = response.body().get(0).getId();
                                Log.d(TAG, "Deleting category with ID: " + categoryId);
                                categoryViewModel.deleteCategory(categoryId, new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(getContext(), "Category deleted successfully.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(getContext(), "Failed to delete category: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Category not found", Toast.LENGTH_SHORT).show();
                            }
                        }


                        @Override
                        public void onFailure(Call<List<Category>> call, Throwable t) {
                            Toast.makeText(getContext(), "Error finding category: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;

                default:
                    Log.e(TAG, "Invalid action: " + action);
            }
        });

        // Show/hide fields based on action
         if("Delete".equals(action)) {;
            categoryName.setVisibility(View.VISIBLE);
            categoryMovies.setVisibility(View.GONE);
            promotedSwitch.setVisibility(View.GONE);
            TextView promotedLabel = view.findViewById(R.id.label_promoted);
            promotedLabel.setVisibility(View.GONE);
        }

        return view;
    }
}
