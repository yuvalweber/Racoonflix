package com.example.netflix.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netflix.database.AppDatabase;
import com.example.netflix.entities.TokenEntity;
import com.example.netflix.models.Category;
import com.example.netflix.models.Movie;
import com.example.netflix.models.User;
import com.example.netflix.api.CategoryServiceApi;
import com.example.netflix.api.MovieServiceApi;
import com.example.netflix.api.UserApiService;
import com.example.netflix.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private static final String TAG = "MovieRepository";

    private final MovieServiceApi movieApi;
    private final CategoryServiceApi categoryApi;
    private final UserApiService userApi;
    private final AppDatabase appDatabase;

    public MovieRepository(Context context) {
        movieApi = RetrofitInstance.getInstance().create(MovieServiceApi.class);
        categoryApi = RetrofitInstance.getInstance().create(CategoryServiceApi.class);
        userApi = RetrofitInstance.getInstance().create(UserApiService.class);
        appDatabase = AppDatabase.getInstance(context);
    }

    public LiveData<Map<String, List<Movie>>> fetchMoviesByCategory() {
        MutableLiveData<Map<String, List<Movie>>> liveData = new MutableLiveData<>();

        new Thread(() -> {
            TokenEntity tokenEntity = appDatabase.tokenDao().getTokenData();
            if (tokenEntity == null) {
                Log.e(TAG, "Token entity is null. Cannot proceed with API calls.");
                liveData.postValue(new HashMap<>()); // Post empty data if no token is found
                return;
            }

            String token = "Bearer " + tokenEntity.getToken();
            String userId = tokenEntity.getUserId();

            movieApi.getMovies(token).enqueue(new Callback<List<Movie>>() {
                @Override
                public void onResponse(Call<List<Movie>> call, Response<List<Movie>> movieResponse) {
                    if (!movieResponse.isSuccessful() || movieResponse.body() == null) {
                        Log.e(TAG, "Failed to fetch movies: " + movieResponse.message());
                        liveData.postValue(new HashMap<>());
                        return;
                    }

                    List<Movie> movies = movieResponse.body();
                    Log.d(TAG, "Movies fetched: " + movies);

                    categoryApi.getCategories(token).enqueue(new Callback<List<Category>>() {
                        @Override
                        public void onResponse(Call<List<Category>> call, Response<List<Category>> categoryResponse) {
                            if (!categoryResponse.isSuccessful() || categoryResponse.body() == null) {
                                Log.e(TAG, "Failed to fetch categories: " + categoryResponse.message());
                                liveData.postValue(new HashMap<>());
                                return;
                            }

                            List<Category> categories = categoryResponse.body();
                            Log.d(TAG, "Categories fetched: " + categories);

                            userApi.getUser(token, userId).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> userResponse) {
                                    if (!userResponse.isSuccessful() || userResponse.body() == null) {
                                        Log.e(TAG, "Failed to fetch user: " + userResponse.message());
                                        liveData.postValue(new HashMap<>());
                                        return;
                                    }

                                    User user = userResponse.body();
                                    Log.d(TAG, "User's seen movies: " + user.getSeenMovies());

                                    Set<String> seenMovieIds = user.getSeenMovies().stream()
                                            .map(movie -> movie.getMovieId())
                                            .collect(Collectors.toSet());

                                    Map<String, List<Movie>> categorizedMovies = new HashMap<>();

                                    // Map movies to categories
                                    for (Movie movie : movies) {
                                        boolean isSeen = seenMovieIds.contains(movie.getId());
                                        Log.d(TAG, "Processing Movie: " + movie.getId() + ", Title: " + movie.getTitle());

                                        for (String categoryId : movie.getCategory()) {
                                            Log.d(TAG, "Checking Category ID: " + categoryId + " for Movie: " + movie.getTitle());

                                            categories.stream()
                                                    .filter(category -> {
                                                        boolean isMatch = categoryId != null && category.getId() != null && category.getId().equals(categoryId);
                                                        if (!isMatch) {
                                                            Log.d(TAG, "No match for Category ID: " + categoryId + " in Category: " + category.getId());
                                                        }
                                                        return isMatch;
                                                    })
                                                    .findFirst()
                                                    .ifPresent(category -> {
                                                        String categoryName = category.getName();
                                                        categorizedMovies.putIfAbsent(categoryName, new ArrayList<>());
                                                        categorizedMovies.get(categoryName).add(movie);
                                                        Log.d(TAG, "Added Movie: " + movie.getTitle() + " to Category: " + categoryName);

                                                        // Add to "Seen Movies" if applicable
                                                        if (isSeen) {
                                                            categorizedMovies.putIfAbsent("Seen Movies", new ArrayList<>());
                                                            categorizedMovies.get("Seen Movies").add(movie);
                                                            Log.d(TAG, "Added Movie: " + movie.getTitle() + " to Seen Movies");
                                                        }
                                                    });
                                        }
                                    }

                                    Log.d(TAG, "Categorized movies: " + categorizedMovies);
                                    liveData.postValue(categorizedMovies);
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Log.e(TAG, "Failed to fetch user: " + t.getMessage(), t);
                                    liveData.postValue(new HashMap<>());
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<List<Category>> call, Throwable t) {
                            Log.e(TAG, "Failed to fetch categories: " + t.getMessage(), t);
                            liveData.postValue(new HashMap<>());
                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Movie>> call, Throwable t) {
                    Log.e(TAG, "Failed to fetch movies: " + t.getMessage(), t);
                    liveData.postValue(new HashMap<>());
                }
            });
        }).start();

        return liveData;
    }
}
