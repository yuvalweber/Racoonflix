package com.example.netflix.repository;

import androidx.lifecycle.MutableLiveData;

public class UserRepository {
    public MutableLiveData<String> login(String userName, String password) {
        MutableLiveData<String> tokenLiveData = new MutableLiveData<>();

        // Simulate a network/API call
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate network delay
                if ("test".equals(userName) && "password".equals(password)) {
                    tokenLiveData.postValue("mock-token-123");
                } else {
                    tokenLiveData.postValue(null); // Login failed
                }
            } catch (InterruptedException e) {
                tokenLiveData.postValue(null);
            }
        }).start();

        return tokenLiveData;
    }
}
