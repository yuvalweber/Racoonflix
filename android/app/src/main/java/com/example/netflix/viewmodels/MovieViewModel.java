package com.example.netflix.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.netflix.models.Category;
import com.example.netflix.models.Movie;
import com.example.netflix.repository.MovieRepository;

import java.io.File;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;

public class MovieViewModel extends AndroidViewModel {

    //creare a tag for logging
    private static final String TAG = "MovieViewModel";
    private final MovieRepository repository;

	//constructor
    public MovieViewModel(Application application) {
        super(application);
        repository = new MovieRepository(application);
    }

    public LiveData<Map<String, List<Movie>>> getMoviesByCategory() {
        return repository.fetchMoviesByCategory();
    }

    public LiveData<Map<String, List<Movie>>> searchMovies(String query) {
        return repository.searchMovies(query);
    }

    public LiveData<Map<String, List<Movie>>> getAllMovies() {
        return repository.fetchAllMovies();
    }

//    public void createMovie(Movie movie, Callback<Void> callback) {
//        repository.createMovie(movie, callback);
//    }

    public void updateMovie(String id, Movie movie, Callback<Void> callback) {
        repository.updateMovie(id, movie, callback);
    }

    public void deleteMovie(String id, Callback<Void> callback) {
        repository.deleteMovie(id, callback);
    }

    public void fetchMovieIdByName(String movieTitle, Callback<List<Movie>> callback) {
        repository.fetchMovieIdByName(movieTitle, callback);
    }

    public List<Movie> fetchMoviesIdByName(String [] movieTitles) {
        return repository.fetchMoviesIdByNameSync(movieTitles);
    }
    public LiveData<List<Movie>> getRecommendedMovies(String movieId) {
        Log.d(TAG, "getRecommendedMovies: " + movieId);
        return repository.fetchRecommendedMovies(movieId);
    }

    public void createMovieWithFiles(Movie movie, File imageFile, File trailerFile, Callback<Void> callback) {
        repository.createMovieWithFiles(movie, imageFile, trailerFile, callback);
    }

}
