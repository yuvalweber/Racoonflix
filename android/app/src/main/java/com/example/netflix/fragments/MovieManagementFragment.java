package com.example.netflix.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieManagementFragment extends Fragment {

    private static final String ARG_ACTION = "action";
    private static final int PICK_IMAGE = 1;
    private static final int PICK_TRAILER = 2;
    private static final String TAG = "MovieManagementFragment";

    private String action;
    private MovieViewModel movieViewModel;
    private CategoryViewModel categoryViewModel;

    private Uri selectedImageUri;
    private Uri selectedTrailerUri;
    private File selectedImageFile;
    private File selectedTrailerFile;

    public static MovieManagementFragment newInstance(String action) {
		// Create a new instance of the fragment with the action passed as an argument
        MovieManagementFragment fragment = new MovieManagementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACTION, action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
		// Get the action from the arguments
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
        Button selectImageButton = view.findViewById(R.id.button_select_movie_image);
        TextView selectedImageText = view.findViewById(R.id.text_selected_movie_image);
        Button selectTrailerButton = view.findViewById(R.id.button_select_movie_trailer);
        TextView selectedTrailerText = view.findViewById(R.id.text_selected_movie_trailer);
        Button submitButton = view.findViewById(R.id.button_submit);

        // File selection for image
        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        // File selection for trailer
        selectTrailerButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_TRAILER);
        });

        // Configure Submit Button Action
        submitButton.setOnClickListener(v -> {
            String movieNameInput = movieTitle.getText().toString().trim();
            String newMovieNameInput = newMovieTitle.getText().toString().trim();
			// Create a new movie object with the input values
            Movie movie = new Movie();
            if (newMovieNameInput.isEmpty()) {
                movie.setTitle(movieNameInput);
            } else {
                movie.setTitle(newMovieNameInput);
            }
            movie.setYear(movieYear.getText().toString().isEmpty() ? 0 : Integer.parseInt(movieYear.getText().toString().trim()));
            movie.setDirector(movieDirector.getText().toString().trim());
            movie.setDuration(movieDuration.getText().toString().isEmpty() ? 0 : Integer.parseInt(movieDuration.getText().toString().trim()));
            if (!movieCategory.getText().toString().isEmpty()) {
                String[] categories = movieCategory.getText().toString().split(",");
                // Remove space from each category
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

            if (selectedImageFile == null || selectedTrailerFile == null) {
                Toast.makeText(getContext(), "Please select both image and trailer files.", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (action) {
				// Create a new movie with the input values
                case "Create":
                    movieViewModel.createMovieWithFiles(movie, selectedImageFile, selectedTrailerFile, new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "Movie created successfully: " + movie);
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

                default:
					// Log an error if the action is invalid
                    Log.e(TAG, "Invalid action: " + action);
            }
        });

        // Show/hide fields based on action
        if (action.equals("Delete")) {
            newMovieTitle.setVisibility(View.GONE);
            movieCategory.setVisibility(View.GONE);
            movieYear.setVisibility(View.GONE);
            movieDirector.setVisibility(View.GONE);
            movieDuration.setVisibility(View.GONE);
            selectImageButton.setVisibility(View.GONE);
            selectTrailerButton.setVisibility(View.GONE);
            selectedImageText.setVisibility(View.GONE);
            selectedTrailerText.setVisibility(View.GONE);
        } else if (action.equals("Create")) {
            newMovieTitle.setVisibility(View.GONE);
        }

        // Invalidate the cache for movies
        AppDatabase appDatabase = AppDatabase.getInstance(getContext());
        Thread thread = new Thread(() -> {
            appDatabase.movieDao().clearMovies();
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == getActivity().RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                selectedImageUri = data.getData();
                selectedImageFile = new File(getPathFromUri(selectedImageUri));
                TextView selectedImageText = getView().findViewById(R.id.text_selected_movie_image);
                selectedImageText.setText(selectedImageFile.getName());
            } else if (requestCode == PICK_TRAILER) {
                selectedTrailerUri = data.getData();
                selectedTrailerFile = new File(getPathFromUri(selectedTrailerUri));
                TextView selectedTrailerText = getView().findViewById(R.id.text_selected_movie_trailer);
                selectedTrailerText.setText(selectedTrailerFile.getName());
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            try (Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(columnIndex);
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        return filePath;
    }
}
