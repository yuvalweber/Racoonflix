package com.example.netflix.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix.R;
import com.example.netflix.models.Movie;

import java.util.List;

public class MovieCardAdapter extends RecyclerView.Adapter<MovieCardAdapter.MovieViewHolder> {

    private final Context context;
    private final List<Movie> movies;

    public MovieCardAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_card, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.movieTitle.setText(movie.getTitle());

        // Set a placeholder or default image
        Drawable placeholder = ContextCompat.getDrawable(context, R.drawable.sample_movie_thumbnail);
        holder.movieThumbnail.setImageDrawable(placeholder);

        // Load the image if a valid URL is provided
        if (movie.getImage() != null && !movie.getImage().isEmpty()) {
            // Assuming movie.getImage() is a URL, fetch the image asynchronously
            new Thread(() -> {
                try {
                    java.net.URL url = new java.net.URL(movie.getImage());
                    android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(url.openStream());
                    holder.movieThumbnail.post(() -> holder.movieThumbnail.setImageBitmap(bitmap));
                } catch (Exception e) {
                    e.printStackTrace();
                    // Keep the placeholder image if loading fails
                }
            }).start();
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle;
        ImageView movieThumbnail;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movie_title);
            movieThumbnail = itemView.findViewById(R.id.movie_thumbnail);
        }
    }
}
