package com.example.netflix.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netflix.api.UserApiService;
import com.example.netflix.database.AppDatabase;
import com.example.netflix.entities.CategoryEntity;
import com.example.netflix.entities.MovieEntity;
import com.example.netflix.entities.TokenEntity;
import com.example.netflix.models.Category;
import com.example.netflix.models.Movie;
import com.example.netflix.api.CategoryServiceApi;
import com.example.netflix.api.MovieServiceApi;
import com.example.netflix.network.RetrofitInstance;
import com.example.netflix.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private static final String TAG = "MovieRepository";

    private final MovieServiceApi movieApi;
    private final CategoryServiceApi categoryApi;
    private final AppDatabase appDatabase;
    private final UserApiService userApi;


    public MovieRepository(Context context) {
        movieApi = RetrofitInstance.getInstance().create(MovieServiceApi.class);
        categoryApi = RetrofitInstance.getInstance().create(CategoryServiceApi.class);
        userApi = RetrofitInstance.getInstance().create(UserApiService.class);
        appDatabase = AppDatabase.getInstance(context);
    }

    // Fetch movies by category (existing functionality intact)
    public LiveData<Map<String, List<Movie>>> fetchMoviesByCategory() {
        MutableLiveData<Map<String, List<Movie>>> liveData = new MutableLiveData<>();

        new Thread(() -> {
            TokenEntity tokenEntity = appDatabase.tokenDao().getTokenData();
            if (tokenEntity == null) {
                Log.e(TAG, "Token entity is null. Cannot proceed with API calls.");
                liveData.postValue(new HashMap<>());
                return;
            }

            String token = "Bearer " + tokenEntity.getToken();
            long currentTime = System.currentTimeMillis();
            long validTime = currentTime - 3600000; // 1 hour in milliseconds

            // Query valid categories from the database
            List<CategoryEntity> localCategories = appDatabase.categoryDao().getValidCategories(validTime);

            if (!localCategories.isEmpty()) {
                // Convert CategoryEntity to Category
                List<Category> categories = localCategories.stream()
                        .map(entity -> {
                            Category category = new Category();
                            category.setId(entity.getCategoryId());
                            category.setName(entity.getName());
                            category.setPromoted(entity.isPromoted());
                            return category;
                        })
                        .collect(Collectors.toList());

                fetchAndCategorizeMoviesByUser(liveData, token, categories, tokenEntity.getUserId());
            } else {
                // Fetch categories from API
                categoryApi.getCategories(token).enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                        if (!response.isSuccessful() || response.body() == null) {
                            Log.e(TAG, "Failed to fetch categories: " + response.message());
                            liveData.postValue(new HashMap<>());
                            return;
                        }

                        List<Category> categories = response.body();

                        // Update the database
                        Thread thread = new Thread( () -> {appDatabase.categoryDao().clearCategories();});
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            Log.e(TAG, "Error clearing categories", e);
                        }
                        List<CategoryEntity> categoryEntities = categories.stream()
                                .map(category -> new CategoryEntity(
                                        category.getId(),
                                        category.getName(),
                                        category.isPromoted(),
                                        System.currentTimeMillis()))
                                .collect(Collectors.toList());
                        Thread thread2 = new Thread(() -> {appDatabase.categoryDao().insertCategories(categoryEntities);});
                        thread2.start();
                        try {
                            thread2.join();
                        } catch (InterruptedException e) {
                            Log.e(TAG, "Error inserting categories", e);
                        }

                        fetchAndCategorizeMoviesByUser(liveData, token, categories, tokenEntity.getUserId());
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {
                        Log.e(TAG, "Error fetching categories from API: " + t.getMessage(), t);
                        liveData.postValue(new HashMap<>());
                    }
                });
            }
        }).start();

        return liveData;
    }

    private void fetchAndCategorizeMoviesByUser(MutableLiveData<Map<String, List<Movie>>> liveData, String token, List<Category> categories, String userId) {
        // check if movies cached in the database
        long currentTime = System.currentTimeMillis();
        long validTime = currentTime - 3600000; // 1 hour in milliseconds
        AtomicReference<List<MovieEntity>> localMovies = new AtomicReference<>(new ArrayList<>());
        Thread thread = new Thread(() -> {
            localMovies.set(appDatabase.movieDao().getValidMovies(validTime));});
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e(TAG, "Error getting movies", e);
        }
        if (!localMovies.get().isEmpty()) {
            // Convert MovieEntity to Movie
            List<Movie> movies = localMovies.get().stream()
                    .map(entity -> {
                        Movie movie = new Movie();
                        movie.setId(entity.getMovieId());
                        movie.setCategory(entity.getCategory());
                        movie.setTitle(entity.getTitle());
                        movie.setYear(entity.getYear());
                        movie.setDirector(entity.getDirector());
                        movie.setDuration(entity.getDuration());
                        movie.setImage(entity.getImage());
                        movie.setTrailer(entity.getTrailer());
                        return movie;
                    })
                    .collect(Collectors.toList());

            Log.d(TAG, "Movies fetched from local database: " + movies);

            userApi.getUser(token, userId).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> userResponse) {
                    if (!userResponse.isSuccessful() || userResponse.body() == null) {
                        Log.e(TAG, "Failed to fetch user: " + userResponse.message());
                        liveData.postValue(new HashMap<>());
                        return;
                    }

                    User user = userResponse.body();
                    Set<String> seenMovieIds = user.getSeenMovies().stream()
                            .map(movie -> movie.getMovieId())
                            .collect(Collectors.toSet());

                    Map<String, List<Movie>> categorizedMovies = categorizeMoviesByCategories(movies, categories, seenMovieIds);
                    liveData.postValue(categorizedMovies);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e(TAG, "Failed to fetch user: " + t.getMessage(), t);
                    liveData.postValue(new HashMap<>());
                }
            });
        } else {
        movieApi.getMovies(token).enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> movieResponse) {
                if (!movieResponse.isSuccessful() || movieResponse.body() == null) {
                    Log.e(TAG, "Failed to fetch movies: " + movieResponse.message());
                    liveData.postValue(new HashMap<>());
                    return;
                }

                List<Movie> movies = movieResponse.body();

                // Update the database
                Thread thread2 = new Thread(() -> {appDatabase.movieDao().clearMovies();});
                thread2.start();
                try {
                    thread2.join();
                } catch (InterruptedException e) {
                    Log.e(TAG, "Error clearing movies", e);
                }

                List<MovieEntity> movieEntities = movies.stream()
                        .map(movie -> new MovieEntity(
                                movie.getId(),
                                movie.getTitle(),
                                movie.getYear(),
                                movie.getDirector(),
                                movie.getCategory(),
                                movie.getDuration(),
                                movie.getImage(),
                                movie.getTrailer(),
                                System.currentTimeMillis()))
                        .collect(Collectors.toList());

                Thread thread3 = new Thread(() -> {appDatabase.movieDao().insertMovies(movieEntities);});
                thread3.start();
                try {
                    thread3.join();
                } catch (InterruptedException e) {
                    Log.e(TAG, "Error inserting movies", e);
                }

                userApi.getUser(token, userId).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> userResponse) {
                        if (!userResponse.isSuccessful() || userResponse.body() == null) {
                            Log.e(TAG, "Failed to fetch user: " + userResponse.message());
                            liveData.postValue(new HashMap<>());
                            return;
                        }

                        User user = userResponse.body();
                        Set<String> seenMovieIds = user.getSeenMovies().stream()
                                .map(movie -> movie.getMovieId())
                                .collect(Collectors.toSet());

                        Map<String, List<Movie>> categorizedMovies = categorizeMoviesByCategories(movies, categories, seenMovieIds);
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
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e(TAG, "Failed to fetch movies: " + t.getMessage(), t);
                liveData.postValue(new HashMap<>());
            }
        });
        }
    }

    private Map<String, List<Movie>> categorizeMoviesByCategories(List<Movie> movies, List<Category> categories, Set<String> seenMovieIds) {
        Map<String, List<Movie>> categorizedMovies = new LinkedHashMap<>();

        for (Movie movie : movies) {
            boolean isSeen = seenMovieIds.contains(movie.getId());
            for (String categoryId : movie.getCategory()) {
                categories.stream()
                        .filter(category -> category.getId() != null && category.getId().equals(categoryId) && category.isPromoted())
                        .findFirst()
                        .ifPresent(category -> {
                            String categoryName = category.getName();
                            categorizedMovies.putIfAbsent(categoryName, new ArrayList<>());
                            categorizedMovies.get(categoryName).add(movie);

                            if (isSeen) {
                                categorizedMovies.putIfAbsent("Seen Movies", new ArrayList<>());
                                categorizedMovies.get("Seen Movies").add(movie);
                            }
                        });
            }
        }

        // Ensure "Seen Movies" is the last category
        if (categorizedMovies.containsKey("Seen Movies")) {
            List<Movie> seenMovies = categorizedMovies.remove("Seen Movies");
            categorizedMovies.put("Seen Movies", seenMovies);
        }

        return categorizedMovies;
    }


    // Fetch all movies
    public LiveData<Map<String, List<Movie>>> fetchAllMovies() {
        MutableLiveData<Map<String, List<Movie>>> liveData = new MutableLiveData<>();

        new Thread(() -> {
            TokenEntity tokenEntity = appDatabase.tokenDao().getTokenData();
            if (tokenEntity == null) {
                Log.e(TAG, "Token entity is null. Cannot proceed with API calls.");
                liveData.postValue(new HashMap<>());
                return;
            }

            String token = "Bearer " + tokenEntity.getToken();
            long currentTime = System.currentTimeMillis();
            long validTime = currentTime - 3600000; // 1 hour in milliseconds

            // Query valid categories from the database
            List<CategoryEntity> localCategories = appDatabase.categoryDao().getValidCategories(validTime);

            if (!localCategories.isEmpty()) {
                // Convert CategoryEntity to Category and use local data
                List<Category> categories = new ArrayList<>();
                for (CategoryEntity categoryEntity : localCategories) {
                    Category category = new Category();
                    category.setId(categoryEntity.getCategoryId());
                    category.setName(categoryEntity.getName());
                    category.setPromoted(categoryEntity.isPromoted());
                    categories.add(category);
                }
                fetchMoviesAndCategorize(liveData, token, categories);
            } else {
                // Fetch categories from API
                categoryApi.getCategories(token).enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                        if (!response.isSuccessful() || response.body() == null) {
                            Log.e(TAG, "Failed to fetch categories: " + response.message());
                            liveData.postValue(new HashMap<>());
                            return;
                        }

                        List<Category> categories = response.body();

                        // Update the database
                        appDatabase.categoryDao().clearCategories();
                        List<CategoryEntity> categoryEntities = categories.stream()
                                .map(category -> new CategoryEntity(
                                        category.getId(),
                                        category.getName(),
                                        category.isPromoted(),
                                        System.currentTimeMillis()))
                                .collect(Collectors.toList());
                        appDatabase.categoryDao().insertCategories(categoryEntities);

                        fetchMoviesAndCategorize(liveData, token, categories);
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {
                        Log.e(TAG, "Error fetching categories from API: " + t.getMessage(), t);
                        liveData.postValue(new HashMap<>());
                    }
                });
            }
        }).start();

        return liveData;
    }

    private void fetchMoviesAndCategorize(MutableLiveData<Map<String, List<Movie>>> liveData, String token, List<Category> categories) {
        movieApi.getAllMovies(token).enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e(TAG, "Failed to fetch all movies: " + response.message());
                    liveData.postValue(new HashMap<>());
                    return;
                }

                List<Movie> movies = response.body();
                Map<String, List<Movie>> categorizedMovies = categorizeMoviesByCategories(movies, categories);
                liveData.postValue(categorizedMovies);
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e(TAG, "Failed to fetch all movies: " + t.getMessage(), t);
                liveData.postValue(new HashMap<>());
            }
        });
    }

    // Search for movies
    public LiveData<Map<String, List<Movie>>> searchMovies(String query) {
        MutableLiveData<Map<String, List<Movie>>> liveData = new MutableLiveData<>();

        new Thread(() -> {
            TokenEntity tokenEntity = appDatabase.tokenDao().getTokenData();
            if (tokenEntity == null) {
                Log.e(TAG, "Token entity is null. Cannot proceed with API calls.");
                liveData.postValue(new HashMap<>());
                return;
            }

            String token = "Bearer " + tokenEntity.getToken();

            movieApi.searchMovies(token, query).enqueue(new Callback<List<Movie>>() {
                @Override
                public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                    if (!response.isSuccessful() || response.body() == null) {
                        Log.e(TAG, "Failed to search movies: " + response.message());
                        liveData.postValue(new HashMap<>());
                        return;
                    }

                    List<Movie> movies = response.body();
                    Log.d(TAG, "Search results fetched: " + movies);

                    // Create a single category for search results
                    Map<String, List<Movie>> searchResults = new HashMap<>();
                    searchResults.put("Search Results", movies);

                    liveData.postValue(searchResults);
                }

                @Override
                public void onFailure(Call<List<Movie>> call, Throwable t) {
                    Log.e(TAG, "Failed to search movies: " + t.getMessage(), t);
                    liveData.postValue(new HashMap<>());
                }
            });
        }).start();

        return liveData;
    }


    // Helper method to categorize movies by categories
    private Map<String, List<Movie>> categorizeMoviesByCategories(List<Movie> movies, List<Category> categories) {
        Map<String, List<Movie>> categorizedMovies = new HashMap<>();

        for (Movie movie : movies) {
            for (String categoryId : movie.getCategory()) {
                categories.stream()
                        .filter(category -> category.getId() != null && category.getId().equals(categoryId))
                        .findFirst()
                        .ifPresent(category -> {
                            String categoryName = category.getName();
                            categorizedMovies.putIfAbsent(categoryName, new ArrayList<>());
                            categorizedMovies.get(categoryName).add(movie);
                        });
            }
        }

        return categorizedMovies;
    }

    public void createMovie(Movie movie, Callback<Void> callback) {
        executeMovieApiCall(api -> api.createMovie(getToken(), movie), callback);
    }

    public void updateMovie(String id, Movie movie, Callback<Void> callback) {
        executeMovieApiCall(api -> api.updateMovie(getToken(), id, movie), callback);
    }

    public void deleteMovie(String id, Callback<Void> callback) {
        executeMovieApiCall(api -> api.deleteMovie(getToken(), id), callback);
    }

    private String getToken() {
        // get the value of token from the thread
        StringBuilder token = new StringBuilder();
        Thread thread = new Thread(() -> {
            TokenEntity tokenEntity = appDatabase.tokenDao().getTokenData();
            if (tokenEntity != null) {
                token.append("Bearer ").append(tokenEntity.getToken());
            }
        });
        thread.start();
        try {
            thread.join();

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

    private void executeMovieApiCall(CallExecutor executor, Callback<Void> callback) {
        String token = getToken();
        if (token != null) {
            executor.execute(movieApi).enqueue(callback);
        } else {
            Log.e(TAG, "Token is null. Cannot proceed with API call.");
        }
    }

    public void fetchMovieIdByName(String movieName, Callback<List<Movie>> callback) {
        new Thread(() -> {
            String token = getToken();
            if (token != null) {
                movieApi.getAllMovies(token).enqueue(new Callback<List<Movie>>() {
                    @Override
                    public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Movie> movies = response.body();
                            for (Movie movie : movies) {
                                if (movie.getTitle().equalsIgnoreCase(movieName)) {
                                    Log.d(TAG, "Movie found: " + movieName + " matching " + movie.getTitle());
                                    List<Movie> result = new ArrayList<>();
                                    result.add(movie);
                                    callback.onResponse(call, Response.success(result));
                                    return;
                                }
                            }
                            Log.e(TAG, "Movie not found: " + movieName);
                            callback.onFailure(call, new Throwable("Movie not found"));
                        } else {
                            Log.e(TAG, "Failed to fetch movies: " + response.message());
                            callback.onFailure(call, new Throwable(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Movie>> call, Throwable t) {
                        Log.e(TAG, "Error fetching movies", t);
                        callback.onFailure(call, t);
                    }
                });
            } else {
                Log.e(TAG, "Token is null. Cannot proceed with API call.");
                callback.onFailure(null, new Throwable("Token is null"));
            }
        }).start();
    }

    public List<Movie> fetchMoviesIdByNameSync(String[] movieNames) {
        Log.d(TAG, "movieName: " + movieNames);
        List<Movie> result = new ArrayList<>();
        String token = getToken();
        if (token != null) {
            try {
                // Perform a synchronous call
                Log.d(TAG, "Fetching movies synchronously");

                // enter the following line to thread
                Thread thread = new Thread(() -> {
                    Response<List<Movie>> response = null;
                    try {
                        response = movieApi.getAllMovies(token).execute();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        List<Movie> movies = response.body();
                        for (String movieName : movieNames) {
                            for (Movie movie : movies) {
                                if (movie.getTitle().equalsIgnoreCase(movieName.trim())) {
                                    Log.d(TAG, "Movie found: " + movieName + " matching " + movie.getTitle());
                                    result.add(movie);
                                    break;
                                }
                            }
                        }
                    } else {
                        Log.e(TAG, "Failed to fetch movies: " + response.message());
                    }
                });
                thread.start();

                thread.join();

            } catch (Exception e) {
                Log.e(TAG, "Error performing synchronous movie fetch", e);
            }
        } else {
            Log.e(TAG, "Token is null. Cannot proceed with API call.");
        }
        return result;
    }

    public LiveData<List<Movie>> fetchRecommendedMovies(String movieId) {
        MutableLiveData<List<Movie>> liveData = new MutableLiveData<>();
        Log.d(TAG, "fetch, movieId: " + movieId);

        new Thread(() -> {
            TokenEntity tokenEntity = appDatabase.tokenDao().getTokenData();
            if (tokenEntity == null) {
                Log.e(TAG, "Token entity is null. Cannot proceed with API call.");
                liveData.postValue(new ArrayList<>());
                return;
            }

            String token = "Bearer " + tokenEntity.getToken();

            movieApi.getRecommendedMovies(token, movieId).enqueue(new Callback<List<Movie>>() {
                @Override
                public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        liveData.postValue(response.body());
                    } else {
                        Log.e(TAG, "Failed to fetch recommended movies: " + response.message());
                        liveData.postValue(new ArrayList<>());
                    }
                }

                @Override
                public void onFailure(Call<List<Movie>> call, Throwable t) {
                    Log.e(TAG, "Error fetching recommended movies: " + t.getMessage(), t);
                    liveData.postValue(new ArrayList<>());
                }
            });
        }).start();

        return liveData;
    }



    @FunctionalInterface
    private interface CallExecutor {
        Call<Void> execute(MovieServiceApi api);
    }
}
