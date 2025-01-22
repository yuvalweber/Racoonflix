package com.example.netflix.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import com.example.netflix.models.User;

public interface UserApiService {
    @POST("/api/users")
    Call<Void> createUser(@Body User user);

    @GET("/api/users/{id}")
    Call<User> getUserById(@Path("id") int userId);
}
