package com.example.netflix.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.netflix.models.Movie;
import com.example.netflix.repository.MovieRepository;

import java.util.List;
import java.util.Map;

public class MovieViewModel extends AndroidViewModel {

    private final MovieRepository repository;

    public MovieViewModel(Application application) {
        super(application);
        repository = new MovieRepository(application);
    }

    public LiveData<Map<String, List<Movie>>> getMoviesByCategory() {
        return repository.fetchMoviesByCategory();
    }
}
