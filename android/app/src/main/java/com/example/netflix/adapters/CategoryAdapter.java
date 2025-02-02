package com.example.netflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix.R;
import com.example.netflix.models.Category;
import com.example.netflix.models.Movie;

import java.util.List;
import java.util.Map;

// Adapter class for displaying categories and their movies in a RecyclerView
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final Map<String, List<Movie>> categorizedMovies;
    private final Context context;

    public CategoryAdapter(Context context, Map<String, List<Movie>> categorizedMovies) {
        this.context = context;
        this.categorizedMovies = categorizedMovies;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_row, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Get the category name and its movies
        String categoryName = (String) categorizedMovies.keySet().toArray()[position];
        List<Movie> movies = categorizedMovies.get(categoryName);

        holder.categoryTitle.setText(categoryName);

        // Setup inner RecyclerView for movies in this category
        MovieCardAdapter movieCardAdapter = new MovieCardAdapter(context, movies);
        holder.recyclerMovies.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerMovies.setAdapter(movieCardAdapter);
    }
	// Return the number of categories
    @Override
    public int getItemCount() {
        return categorizedMovies.size();
    }
	//

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        RecyclerView recyclerMovies;
		// Initialize the views
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.category_title);
            recyclerMovies = itemView.findViewById(R.id.recycler_movies);
        }
    }
}
