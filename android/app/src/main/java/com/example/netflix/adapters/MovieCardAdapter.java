package com.example.netflix.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import com.example.netflix.MovieDetailsActivity;
import com.example.netflix.R;
import com.example.netflix.models.Movie;

import java.util.List;

// Adapter class for displaying movie cards in RecyclerView
public class MovieCardAdapter extends RecyclerView.Adapter<MovieCardAdapter.MovieViewHolder> {
	// Context and list of movies
    private final Context context;
    private final List<Movie> movies;
	
	// Constructor
    public MovieCardAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
	// Create new views (invoked by the layout manager)
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_card, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    // Replace the contents of a view (invoked by the layout manager)
	public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.movieTitle.setText(movie.getTitle());

        // Load image using Glide
        Glide.with(context)
                .load(movie.getImage())  // movie.getImage() should return the image URL
                .placeholder(R.drawable.sample_movie_thumbnail) // Set placeholder image
                .error(R.drawable.sample_movie_thumbnail) // Set error image
                .into(holder.movieThumbnail);

        // Set click listener for opening MovieDetailsActivity
        holder.movieThumbnail.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("MOVIE_DETAILS", movie);
            context.startActivity(intent);
        });
    }
	

    @Override
	// Return the size of your dataset (invoked by the layout manager)
    public int getItemCount() {
        return movies.size();
    }
	// ViewHolder class for movie card
    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle;
        ImageView movieThumbnail;
		// Constructor
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movie_title);
            movieThumbnail = itemView.findViewById(R.id.movie_thumbnail);
        }
    }
}
