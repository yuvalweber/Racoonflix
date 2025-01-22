package com.example.netflix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix.models.User;
import com.example.netflix.network.RetrofitInstance;
import com.example.netflix.api.UserApiService;
import com.example.netflix.repository.UserRepository;
import com.example.netflix.database.AppDatabase;
import com.example.netflix.viewmodels.UserViewModel;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstNameInput, lastNameInput, userNameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button registerButton;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize input fields
        firstNameInput = findViewById(R.id.editTextFirstName);
        lastNameInput = findViewById(R.id.editTextLastName);
        userNameInput = findViewById(R.id.editTextUsername);
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        confirmPasswordInput = findViewById(R.id.editTextConfirmPassword);
        registerButton = findViewById(R.id.buttonRegister);

        // Initialize API, Repository, and ViewModel
        UserApiService apiService = RetrofitInstance.getInstance().create(UserApiService.class);
        AppDatabase database = AppDatabase.getInstance(this);
        UserRepository userRepository = new UserRepository(apiService, database.tokenDao());
        userViewModel = new UserViewModel(userRepository);

        // Handle register button click
        registerButton.setOnClickListener(v -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String userName = userNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            // Validate inputs
            if (firstName.isEmpty() || lastName.isEmpty() || userName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create the user
            User newUser = new User(firstName, lastName, userName, email, password, null, null, null, null, null);
            userViewModel.createUser(newUser, new UserRepository.UserCallback() {
                @Override
                public void onSuccess(Object data) {
                    Toast.makeText(RegisterActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                // onError method is for application level errors (400...500)
                @Override
                public void onError(int statusCode, String errorMessage) {
                    Toast.makeText(RegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }

                // onFailure method is for network level errors
                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
