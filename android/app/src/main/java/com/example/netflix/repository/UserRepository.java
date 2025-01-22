package com.example.netflix.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.netflix.api.UserApiService;
import com.example.netflix.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private final UserApiService userApiService;

    public UserRepository(UserApiService userApiService) {
        this.userApiService = userApiService;
    }

    // Create a new user
    public void createUser(User user, UserCallback callback) {
        userApiService.createUser(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code());
                } else {
                    String errorMessage = parseErrorBody(response);
                    callback.onError(response.code(), errorMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    // Get user by ID
    public void getUserById(int userId, UserCallback callback) {
        userApiService.getUserById(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    // Parse the error body from the response
    private String parseErrorBody(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                JSONObject jsonObject = new JSONObject(errorBody);
                return jsonObject.optString("errors", "An unknown error occurred.");
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error parsing error body: " + e.getMessage(), e);
        }
        return "An unknown error occurred.";
    }

    // Callback interface for user-related operations
    public interface UserCallback {
        void onSuccess(Object data);
        void onError(int statusCode, String errorMessage);
        void onFailure(Throwable t);
    }
}
