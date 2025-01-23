package com.example.netflix;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.netflix.repository.CategoryRepository;
import com.example.netflix.models.Category;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.netflix.models.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ImageView movieImageView = findViewById(R.id.movieImageView);
        TextView movieTitleTextView = findViewById(R.id.movieTitleTextView);
        TextView movieDetailsTextView = findViewById(R.id.movieDetailsTextView);
        ImageButton playMovieButton = findViewById(R.id.playMovieButton);
        ImageButton closeButton = findViewById(R.id.closeButton);

        // Initialize CategoryRepository
        CategoryRepository categoryRepository = new CategoryRepository(this);

        Movie movie = (Movie) getIntent().getSerializableExtra("MOVIE_DETAILS");

        if (movie != null) {
            movieTitleTextView.setText(movie.getTitle());

            // Fetch and translate categories
            categoryRepository.fetchCategories().observe(this, categories -> {
                if (categories != null) {
                    List<String> categoryNames = movie.getCategory().stream()
                            .map(categoryId -> categories.stream()
                                    .filter(category -> category.getId().equals(categoryId))
                                    .map(Category::getName)
                                    .findFirst()
                                    .orElse("Unknown"))
                            .collect(Collectors.toList());

                    String categoriesString = String.join(", ", categoryNames);

                    movieDetailsTextView.setText(String.format(
                            "Year:\t%d\nDirector:\t%s\nDuration:\t%d mins\nCategories:\t%s",
                            movie.getYear(),
                            movie.getDirector(),
                            movie.getDuration(),
                            categoriesString
                    ));
                }
            });

            Glide.with(this).load(movie.getImage()).into(movieImageView);
            playMovieButton.setOnClickListener(v -> {
                Intent playIntent = new Intent(MovieDetailsActivity.this, MoviePlayerActivity.class);
                playIntent.putExtra("TRAILER_URL", movie.getTrailer());
                startActivity(playIntent);
            });
        }

        closeButton.setOnClickListener(v -> finish());
    }
}
