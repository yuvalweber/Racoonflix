package com.example.netflix.api;

import com.example.netflix.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CategoryServiceApi {

    @GET("/api/categories")
    Call<List<Category>> getCategories(@Header("Authorization") String token);

    @POST("/api/categories")
    Call<Void> createCategory(@Header("Authorization") String token, @Body Category category);

    @PATCH("/api/categories/{id}")
    Call<Void> updateCategory(@Header("Authorization") String token, @Path("id") String id, @Body Category category);

    @DELETE("/api/categories/{id}")
    Call<Void> deleteCategory(@Header("Authorization") String token, @Path("id") String id);
}
