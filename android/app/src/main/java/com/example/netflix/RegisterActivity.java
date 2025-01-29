package com.example.netflix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix.models.User;
import com.example.netflix.network.RetrofitInstance;
import com.example.netflix.api.UserApiService;
import com.example.netflix.repository.UserRepository;
import com.example.netflix.database.AppDatabase;
import com.example.netflix.viewmodels.UserViewModel;

import java.io.File;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 100;

    private EditText firstNameInput, lastNameInput, userNameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button registerButton, selectProfilePictureButton;
    private TextView selectedProfilePictureText;

    private UserViewModel userViewModel;
    private Uri selectedImageUri;
    private File selectedImageFile;

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
        selectProfilePictureButton = findViewById(R.id.buttonSelectProfilePicture);
        selectedProfilePictureText = findViewById(R.id.textSelectedProfilePicture);

        // Initialize API, Repository, and ViewModel
        UserApiService apiService = RetrofitInstance.getInstance().create(UserApiService.class);
        AppDatabase database = AppDatabase.getInstance(this);
        UserRepository userRepository = new UserRepository(apiService, database.tokenDao());
        userViewModel = new UserViewModel(userRepository);

        // Check permissions
        checkStoragePermission();

        // Handle profile picture selection
        selectProfilePictureButton.setOnClickListener(v -> {
            if (isStoragePermissionGranted()) {
                openImagePicker();
            } else {
                requestStoragePermission();
            }
        });

        // Handle register button click
        registerButton.setOnClickListener(v -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String userName = userNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (validateInputs(firstName, lastName, userName, email, password, confirmPassword)) {
                User newUser = new User(firstName, lastName, userName, email, password, null, null, null, null, null);
                userViewModel.createUser(newUser, selectedImageFile, new UserRepository.UserCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        Toast.makeText(RegisterActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }

                    @Override
                    public void onError(int statusCode, String errorMessage) {
                        Toast.makeText(RegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            selectedImageFile = new File(getPathFromUri(selectedImageUri));
            selectedProfilePictureText.setText(selectedImageFile.getName());
        }
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isStoragePermissionGranted()) {
                requestStoragePermission();
            }
        }
    }

    private boolean isStoragePermissionGranted() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs(String firstName, String lastName, String userName, String email, String password, String confirmPassword) {
        if (firstName.isEmpty() || lastName.isEmpty() || userName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private String getPathFromUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        return filePath;
    }

}
