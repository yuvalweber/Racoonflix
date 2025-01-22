package com.example.netflix.models;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String profilePicture;
    private Date createdAt;
    private List<SeenMovie> seenMovies;
    private Date updatedAt;
    private boolean isAdmin;

    // Constructor
    public User(String firstName, String lastName, String userName, String email,
                String password, String profilePicture, Date createdAt, List<SeenMovie> seenMovies,
                Date updatedAt, Boolean isAdmin) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.createdAt = createdAt;
        this.seenMovies = seenMovies;
        this.updatedAt = updatedAt;
        this.isAdmin = isAdmin != null && isAdmin;
    }

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public List<SeenMovie> getSeenMovies() { return seenMovies; }
    public void setSeenMovies(List<SeenMovie> seenMovies) { this.seenMovies = seenMovies; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }
}
