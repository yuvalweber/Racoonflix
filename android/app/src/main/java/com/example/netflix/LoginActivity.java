package com.example.netflix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix.entities.TokenEntity;
import com.example.netflix.database.AppDatabase;
import com.example.netflix.network.RetrofitInstance;
import com.example.netflix.api.UserApiService;
import com.example.netflix.repository.UserRepository;

public class LoginActivity extends AppCompatActivity {
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editTextUserName = findViewById(R.id.editTextUserName);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        // Initialize UserRepository
        UserApiService apiService = RetrofitInstance.getInstance().create(UserApiService.class);
        AppDatabase database = AppDatabase.getInstance(this);
        userRepository = new UserRepository(apiService, database.tokenDao());

        // Handle Login
        buttonLogin.setOnClickListener(v -> {
            String userName = editTextUserName.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (userName.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            userRepository.login(userName, password, new UserRepository.UserCallback() {
                @Override
                public void onSuccess(Object data) {
                    // Fetch token data on a background thread
                    new Thread(() -> {
                        TokenEntity tokenEntity = userRepository.getTokenData();
                        runOnUiThread(() -> {
                            if (tokenEntity != null) {
                                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Prevent returning to LoginActivity
                            } else {
                                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                }

                @Override
                public void onError(int statusCode, String errorMessage) {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onFailure(Throwable t) {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }
            });
        });

        // Navigate to RegisterActivity
        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
