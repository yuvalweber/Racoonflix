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
	private final UserApiService userApiService; // API service for user-related operations
	private final TokenDao tokenDao; // DAO for token-related operations

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
					callback.onSuccess(response.code()); // Notify success with response code
				} else {
					String errorMessage = parseErrorBody(response); // Parse error message
					callback.onError(response.code(), errorMessage); // Notify error with code and message
				}
			}

			@Override
			public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
				callback.onFailure(t); // Notify failure with throwable
			}
		});
	}

	// Login and store token in Room, then fetch userId and isAdmin
	public void login(String userName, String password, UserCallback callback) {
		Map<String, String> loginRequest = new HashMap<>();
		loginRequest.put("userName", userName); // Add username to request
		loginRequest.put("password", password); // Add password to request

		userApiService.login(loginRequest).enqueue(new Callback<Map<String, String>>() {
			@Override
			public void onResponse(@NonNull Call<Map<String, String>> call, @NonNull Response<Map<String, String>> response) {
				if (response.isSuccessful() && response.body() != null) {
					String token = response.body().get("token"); // Extract token from response
					if (token != null) {
						// Save the token in Room
						Thread thread = new Thread(() -> {
							tokenDao.clearTokens(); // Clear existing tokens
							tokenDao.insertToken(new TokenEntity(token, "", false)); // Insert new token
							// Fetch token info after login
							fetchTokenInfo(callback); // Fetch additional token info
						});
						thread.start();
						// wait for the thread to finish
						try {
							thread.join(); // Wait for the thread to finish
						} catch (InterruptedException e) {
							Log.e(TAG, "Thread interrupted: " + e.getMessage(), e); // Log interruption
						}
					} else {
						callback.onError(response.code(), "Token missing in response"); // Notify error if token is missing
					}
				} else {
					String errorMessage = parseErrorBody(response); // Parse error message
					callback.onError(response.code(), errorMessage); // Notify error with code and message
				}
			}

			@Override
			public void onFailure(@NonNull Call<Map<String, String>> call, @NonNull Throwable t) {
				Log.e(TAG, "Login failed: " + t.getMessage(), t); // Log failure
				callback.onFailure(t); // Notify failure with throwable
			}
		});
	}

	// Fetch userId and isAdmin using the token
	public void fetchTokenInfo(UserCallback callback) {
		new Thread(() -> {
			TokenEntity storedToken = tokenDao.getTokenData(); // Retrieve stored token
			if (storedToken == null) {
				runOnMainThread(() -> callback.onError(401, "No token available")); // Notify error if no token is available
				return;
			}

			String authHeader = "Bearer " + storedToken.getToken(); // Create authorization header
			userApiService.getTokenInfo(authHeader).enqueue(new Callback<Map<String, Object>>() {
				@Override
				public void onResponse(@NonNull Call<Map<String, Object>> call, @NonNull Response<Map<String, Object>> response) {
					if (response.isSuccessful() && response.body() != null) {
						Map<String, Object> tokenInfo = response.body(); // Extract token info from response
						String userId = (String) tokenInfo.get("id"); // Extract user ID
						boolean isAdmin = (boolean) tokenInfo.get("isAdmin"); // Extract admin status

						new Thread(() -> {
							storedToken.setUserId(userId); // Set user ID in token
							storedToken.setAdmin(isAdmin); // Set admin status in token
							tokenDao.clearTokens(); // Clear existing tokens
							tokenDao.insertToken(storedToken); // Insert updated token
						}).start();

						runOnMainThread(() -> callback.onSuccess(tokenInfo)); // Notify success with token info
					} else {
						String errorMessage = response.message(); // Extract error message
						runOnMainThread(() -> callback.onError(response.code(), "Failed to fetch token info: " + errorMessage)); // Notify error with code and message
					}
				}

				@Override
				public void onFailure(@NonNull Call<Map<String, Object>> call, @NonNull Throwable t) {
					Log.e(TAG, "Fetching token info failed: " + t.getMessage(), t); // Log failure
					runOnMainThread(() -> callback.onFailure(t)); // Notify failure with throwable
				}
			});
		}).start();
	}

	// Get user by ID
	public void getUserById(String userId, String token, UserCallback callback) {
		String authHeader = "Bearer " + token; // Create authorization header
		userApiService.getUser(authHeader, userId).enqueue(new Callback<User>() {
			@Override
			public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
				if (response.isSuccessful() && response.body() != null) {
					callback.onSuccess(response.body()); // Notify success with user data
				} else {
					callback.onError(response.code(), "Failed to fetch user"); // Notify error with code and message
				}
			}

			@Override
			public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
				callback.onFailure(t); // Notify failure with throwable
			}
		});
	}

	// Retrieve token from Room
	public TokenEntity getTokenData() {
		return tokenDao.getTokenData(); // Return stored token data
	}

	// Parse error body to extract meaningful error messages
	private String parseErrorBody(Response<?> response) {
		try {
			if (response.errorBody() != null) {
				String errorBody = response.errorBody().string(); // Extract error body as string
				JSONObject jsonObject = new JSONObject(errorBody); // Parse error body as JSON
				return jsonObject.optString("errors", "An unknown error occurred."); // Extract error message
			}
		} catch (IOException | JSONException e) {
			Log.e(TAG, "Error parsing error body: " + e.getMessage(), e); // Log parsing error
		}
		return "An unknown error occurred."; // Return default error message
	}

	// Helper to execute callbacks on the main thread
	private void runOnMainThread(Runnable runnable) {
		new Handler(Looper.getMainLooper()).post(runnable); // Post runnable to main thread handler
	}

	public interface UserCallback {
		void onSuccess(Object data); // Callback for success
		void onError(int statusCode, String errorMessage); // Callback for error
		void onFailure(Throwable t); // Callback for failure
	}
}
