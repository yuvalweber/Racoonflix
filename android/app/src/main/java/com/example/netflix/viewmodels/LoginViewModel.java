package com.example.netflix.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.netflix.repository.UserRepository;

public class LoginViewModel extends ViewModel {
    private final UserRepository repository;
    private final MutableLiveData<String> tokenLiveData = new MutableLiveData<>();

    public LoginViewModel() {
        repository = new UserRepository();
    }

    public LiveData<String> getTokenLiveData() {
        return tokenLiveData;
    }

    public void login(String userName, String password) {
        repository.login(userName, password).observeForever(token -> tokenLiveData.setValue(token));
    }
}
