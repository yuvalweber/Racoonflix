package com.example.netflix.repository;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.netflix.api.UserApiService;
import com.example.netflix.models.User;
import com.example.netflix.entities.TokenEntity;
import com.example.netflix.dao.TokenDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private final UserApiService userApiService;
    private final TokenDao tokenDao;

    public UserRepository(UserApiService userApiService, TokenDao tokenDao) {
        this.userApiService = userApiService;
        this.tokenDao = tokenDao;
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

    // Login and store token in Room, then fetch userId and isAdmin
    public void login(String userName, String password, UserCallback callback) {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("userName", userName);
        loginRequest.put("password", password);

        userApiService.login(loginRequest).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, String>> call, @NonNull Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().get("token");
                    if (token != null) {
                        // Save the token in Room
                        new Thread(() -> {
                            tokenDao.clearTokens();
                            tokenDao.insertToken(new TokenEntity(token, "", false));
                        }).start();

                        // Fetch token info after login
                        fetchTokenInfo(callback);
                    } else {
                        callback.onError(response.code(), "Token missing in response");
                    }
                } else {
                    String errorMessage = parseErrorBody(response);
                    callback.onError(response.code(), errorMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, String>> call, @NonNull Throwable t) {
                Log.e(TAG, "Login failed: " + t.getMessage(), t);
                callback.onFailure(t);
            }
        });
    }

    // Fetch userId and isAdmin using the token
    public void fetchTokenInfo(UserCallback callback) {
        new Thread(() -> {
            TokenEntity storedToken = tokenDao.getTokenData();
            if (storedToken == null) {
                runOnMainThread(() -> callback.onError(401, "No token available"));
                return;
            }

            String authHeader = "Bearer " + storedToken.getToken();
            userApiService.getTokenInfo(authHeader).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(@NonNull Call<Map<String, Object>> call, @NonNull Response<Map<String, Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Map<String, Object> tokenInfo = response.body();
                        String userId = (String) tokenInfo.get("id");
                        boolean isAdmin = (boolean) tokenInfo.get("isAdmin");

                        new Thread(() -> {
                            storedToken.setUserId(userId);
                            storedToken.setAdmin(isAdmin);
                            tokenDao.clearTokens();
                            tokenDao.insertToken(storedToken);
                        }).start();

                        runOnMainThread(() -> callback.onSuccess(tokenInfo));
                    } else {
                        String errorMessage = response.message();
                        runOnMainThread(() -> callback.onError(response.code(), "Failed to fetch token info: " + errorMessage));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Map<String, Object>> call, @NonNull Throwable t) {
                    Log.e(TAG, "Fetching token info failed: " + t.getMessage(), t);
                    runOnMainThread(() -> callback.onFailure(t));
                }
            });
        }).start();
    }

    // Get user by ID
    public void getUserById(String userId, String token, UserCallback callback) {
        String authHeader = "Bearer " + token;
        userApiService.getUser(authHeader, userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(response.code(), "Failed to fetch user");
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    // Retrieve token from Room
    public TokenEntity getTokenData() {
        return tokenDao.getTokenData();
    }

    // Parse error body to extract meaningful error messages
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

    // Helper to execute callbacks on the main thread
    private void runOnMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public interface UserCallback {
        void onSuccess(Object data);
        void onError(int statusCode, String errorMessage);
        void onFailure(Throwable t);
    }
}
