package com.example.netflix.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.netflix.models.User;
import com.example.netflix.repository.UserRepository;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create a new user
    public void createUser(User user, UserRepository.UserCallback callback) {
        userRepository.createUser(user, callback);
    }

    // Fetch a user by ID
    public void getUserById(int userId, UserRepository.UserCallback callback) {
        userRepository.getUserById(userId, callback);
    }
}
