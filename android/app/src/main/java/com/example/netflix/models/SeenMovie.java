package com.example.netflix.models;
import java.util.Date;

public class SeenMovie {
    private String movieId; // Referenced movie ID
    private Date watchedAt;

    // Constructor where a date is explicitly provided
    public SeenMovie(String movieId, Date watchedAt) {
        this.movieId = movieId;
        this.watchedAt = watchedAt != null ? watchedAt : new Date(); // Use provided date or current date
    }

    // Constructor where no date is provided (defaults to the current date)
    public SeenMovie(String movieId) {
        this.movieId = movieId;
        this.watchedAt = new Date(); // Default to current date
    }

    // Getters and Setters
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Date getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(Date watchedAt) {
        this.watchedAt = watchedAt;
    }
}
