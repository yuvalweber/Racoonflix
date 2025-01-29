package com.example.netflix.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import com.example.netflix.models.User;

import java.util.Map;

public interface UserApiService {
    // POST to create a new user
    @Multipart
    @POST("/api/users")
    Call<Void> createUser(
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("userName") RequestBody userName,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part profilePicture
    );

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
