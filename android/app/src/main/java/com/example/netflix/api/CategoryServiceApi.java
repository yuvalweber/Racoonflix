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

	// Get all categories
    @GET("/api/categories")
    Call<List<Category>> getCategories(@Header("Authorization") String token);

	// Get a category by id
    @POST("/api/categories")
    Call<Void> createCategory(@Header("Authorization") String token, @Body Category category);

	// update a category by id
    @PATCH("/api/categories/{id}")
    Call<Void> updateCategory(@Header("Authorization") String token, @Path("id") String id, @Body Category category);

	// delete a category by id
    @DELETE("/api/categories/{id}")
    Call<Void> deleteCategory(@Header("Authorization") String token, @Path("id") String id);
}
