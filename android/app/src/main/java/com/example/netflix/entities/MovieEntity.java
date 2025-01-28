package com.example.netflix.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "movies")
public class MovieEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String movieId;
    private String title;
    private int year;
    private String director;

    private List<String> category; // List of category IDs

    private int duration;

    private String image;

    private String trailer;
    private long timestamp;


    public MovieEntity(String movieId, String title, int year, String director, List<String> category, int duration, String image, String trailer, long timestamp) {
        this.movieId = movieId;
        this.title = title;
        this.year = year;
        this.director = director;
        this.category = category;
        this.duration = duration;
        this.image = image;
        this.trailer = trailer;
        this.timestamp = timestamp;
    }
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
