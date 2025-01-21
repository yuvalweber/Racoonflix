package com.example.netflix;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.netflix.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editTextUserName = findViewById(R.id.editTextUserName);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        viewModel.getTokenLiveData().observe(this, token -> {
            if (token != null) {
                Toast.makeText(this, "Login successful! Token: " + token, Toast.LENGTH_SHORT).show();
                // Handle successful login
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });


        buttonLogin.setOnClickListener(v -> {
            String userName = editTextUserName.getText().toString();
            String password = editTextPassword.getText().toString();
            viewModel.login(userName, password);
        });


    }
}
