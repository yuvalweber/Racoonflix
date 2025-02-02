package com.example.netflix.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tokens")
public class TokenEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String token;
    private String userId;
    private boolean isAdmin;

	// Constructor
    public TokenEntity(String token, String userId, boolean isAdmin) {
        this.token = token;
        this.userId = userId;
        this.isAdmin = isAdmin;
    }
	// Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
