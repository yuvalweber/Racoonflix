package com.example.netflix.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import com.example.netflix.models.User;

import java.util.Map;

public interface UserApiService {
    // POST to create a new user
    @POST("/api/users")
    Call<Void> createUser(@Body com.example.netflix.models.User user);

    // GET user details by ID
    @GET("/api/users/{id}")
    Call<User> getUser(@Header("Authorization") String token, @Path("id") String userId);

    // POST to log in and retrieve a token
    @POST("/api/tokens")
    Call<Map<String, String>> login(@Body Map<String, String> loginRequest);

    // GET to retrieve userId and isAdmin based on token
    @GET("/api/tokens")
    Call<Map<String, Object>> getTokenInfo(@Header("Authorization") String authHeader);
}
