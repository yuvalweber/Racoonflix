package com.example.netflix.models;

import java.util.List;
import java.io.Serializable;

public class Movie implements Serializable {
    private String id;    // Primary field for app logic
    private String _id;   // Maps directly to the API response
    private String title;
    private int year;
    private List<String> category; // List of category IDs
    private String director;
    private int duration;
    private String image;
    private String trailer;

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

    // Getter and Setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and Setter for year
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // Getter and Setter for category
    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    // Getter and Setter for director
    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    // Getter and Setter for duration
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // Getter and Setter for image
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Getter and Setter for trailer
    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + getId() + '\'' + // Use getId() to display the resolved ID
                ", title='" + title + '\'' +
                ", year=" + year +
                ", category=" + category +
                ", director='" + director + '\'' +
                ", duration=" + duration +
                ", image='" + image + '\'' +
                ", trailer='" + trailer + '\'' +
                '}';
    }
}
