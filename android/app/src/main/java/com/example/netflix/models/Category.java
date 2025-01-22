package com.example.netflix.models;

import java.util.List;

public class Category {
    private String id;    // Primary field for app logic
    private String _id;   // Maps directly to the API response
    private String name;
    private boolean promoted;
    private List<String> movies; // List of movie IDs in this category

    // Getter for id
    public String getId() {
        return id != null ? id : _id; // Use id if available, fallback to _id
    }

    // Setter for id
    public void setId(String id) {
        this.id = id;
    }

    // Getter for _id
    public String get_id() {
        return _id;
    }

    // Setter for _id
    public void set_id(String _id) {
        this._id = _id;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for promoted
    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    // Getter and Setter for movies
    public List<String> getMovies() {
        return movies;
    }

    public void setMovies(List<String> movies) {
        this.movies = movies;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + getId() + '\'' + // Use getId() to display the resolved ID
                ", name='" + name + '\'' +
                ", promoted=" + promoted +
                ", movies=" + movies +
                '}';
    }
}
