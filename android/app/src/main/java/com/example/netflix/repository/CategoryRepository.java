package com.example.netflix.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netflix.api.CategoryServiceApi;
import com.example.netflix.database.AppDatabase;
import com.example.netflix.entities.TokenEntity;
import com.example.netflix.models.Category;
import com.example.netflix.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

	private static final String TAG = "CategoryRepository";
	private final CategoryServiceApi categoryApi; // API service for categories
	private final AppDatabase appDatabase; // Local database instance

	public CategoryRepository(Context context) {
		categoryApi = RetrofitInstance.getInstance().create(CategoryServiceApi.class); // Initialize API service
		appDatabase = AppDatabase.getInstance(context); // Initialize database
	}

	public LiveData<List<Category>> fetchCategories() {
		MutableLiveData<List<Category>> liveData = new MutableLiveData<>();
		new Thread(() -> {
			TokenEntity tokenEntity = appDatabase.tokenDao().getTokenData(); // Fetch token from database
			if (tokenEntity != null) {
				String token = "Bearer " + tokenEntity.getToken();
				categoryApi.getCategories(token).enqueue(new Callback<List<Category>>() {
					@Override
					public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
						if (response.isSuccessful()) {
							liveData.postValue(response.body()); // Post fetched categories to LiveData
						} else {
							Log.e(TAG, "Failed to fetch categories: " + response.message());
						}
					}

					@Override
					public void onFailure(Call<List<Category>> call, Throwable t) {
						Log.e(TAG, "Error fetching categories", t);
					}
				});
			}
		}).start();
		return liveData;
	}

	public void createCategory(Category category, Callback<Void> callback) {
		executeCategoryApiCall(api -> api.createCategory(getToken(), category), callback); // Create category API call
	}

	public void updateCategory(String id, Category category, Callback<Void> callback) {
		executeCategoryApiCall(api -> api.updateCategory(getToken(), id, category), callback); // Update category API call
	}

	public void deleteCategory(String id, Callback<Void> callback) {
		executeCategoryApiCall(api -> api.deleteCategory(getToken(), id), callback); // Delete category API call
	}

	private String getToken() {
		// Get the value of token from the thread
		StringBuilder token = new StringBuilder();
		Thread thread = new Thread(() -> {
			TokenEntity tokenEntity = appDatabase.tokenDao().getTokenData(); // Fetch token from database
			if (tokenEntity != null) {
				token.append("Bearer ").append(tokenEntity.getToken());
			}
		});
		thread.start();
		try {
			thread.join(); // Wait for the thread to finish
		} catch (InterruptedException e) {
			Log.e(TAG, "Error getting token", e);
		}
		if (token.length() == 0) {
			Log.e(TAG, "Token is null. Cannot proceed with API call.");
			return null;
		} else {
			return token.toString();
		}
	}

	private void executeCategoryApiCall(CallExecutor executor, Callback<Void> callback) {
		String token = getToken();
		if (token != null) {
			executor.execute(categoryApi).enqueue(callback); // Execute API call with token
		} else {
			Log.e(TAG, "Token is null. Cannot proceed with API call.");
		}
	}

	public void fetchCategoryIdByName(String categoryName, Callback<List<Category>> callback) {
		new Thread(() -> {
			String token = getToken();
			if (token != null) {
				categoryApi.getCategories(token).enqueue(new Callback<List<Category>>() {
					@Override
					public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
						if (response.isSuccessful() && response.body() != null) {
							List<Category> categories = response.body();
							for (Category category : categories) {
								if (category.getName().equalsIgnoreCase(categoryName)) {
									Log.d(TAG, "Category found: " + categoryName + " matching " + category.getName());
									List<Category> result = new ArrayList<>();
									result.add(category);
									Log.d(TAG, "Category found: " + result);
									callback.onResponse(call, Response.success(result)); // Return found category
									return;
								}
							}
							Log.e(TAG, "Category not found: " + categoryName);
							callback.onFailure(call, new Throwable("Category not found"));
						} else {
							Log.e(TAG, "Failed to fetch categories: " + response.message());
							callback.onFailure(call, new Throwable(response.message()));
						}
					}

					@Override
					public void onFailure(Call<List<Category>> call, Throwable t) {
						Log.e(TAG, "Error fetching categories", t);
						callback.onFailure(call, t);
					}
				});
			} else {
				Log.e(TAG, "Token is null. Cannot proceed with API call.");
				callback.onFailure(null, new Throwable("Token is null"));
			}
		}).start();
	}

	public List<Category> fetchCategoriesIdByNameSync(String[] categoryNames) {
		List<Category> result = new ArrayList<>();
		String token = getToken();
		if (token != null) {
			try {
				Thread thread = new Thread(() -> {
					Response<List<Category>> response = null;
					try {
						response = categoryApi.getCategories(token).execute(); // Synchronous API call
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					if (response.isSuccessful() && response.body() != null) {
						List<Category> categories = response.body();
						for (Category category : categories) {
							for (String categoryName : categoryNames) {
								if (category.getName().equalsIgnoreCase(categoryName)) {
									result.add(category); // Add matching category to result
									break;
								}
							}
						}
					} else {
						Log.e(TAG, "Failed to fetch categories: " + response.message());
					}
				});
				thread.start();
				thread.join(); // Wait for the thread to finish
			} catch (Exception e) {
				Log.e(TAG, "Error fetching categories", e);
			}
		}
		return result;
	}

	@FunctionalInterface
	private interface CallExecutor {
		Call<Void> execute(CategoryServiceApi api); // Functional interface for API call execution
	}
}
