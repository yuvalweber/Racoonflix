package com.example.netflix.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.netflix.models.User;
import com.example.netflix.entities.TokenEntity;
import com.example.netflix.repository.UserRepository;

import java.io.File;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create a new user
    public void createUser(User user, File profilePicture, UserRepository.UserCallback callback) {
        userRepository.createUser(user, profilePicture, callback);
    }


    // Fetch a user by ID
    public void getUserById(String userId, String token, UserRepository.UserCallback callback) {
        userRepository.getUserById(userId, token, callback);
    }

    // Login and retrieve a token
    public void login(String userName, String password, UserRepository.UserCallback callback) {
        userRepository.login(userName, password, callback);
    }

    // Fetch token information
    public void fetchTokenInfo(UserRepository.UserCallback callback) {
        userRepository.fetchTokenInfo(callback);
    }

    // Retrieve token data from Room
    public TokenEntity getTokenData() {
        return userRepository.getTokenData();
    }
}
