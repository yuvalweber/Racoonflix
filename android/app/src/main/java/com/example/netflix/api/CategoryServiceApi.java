package com.example.netflix.api;

import com.example.netflix.models.Category;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CategoryServiceApi {
    @GET("/api/categories")
    Call<List<Category>> getCategories(@Header("Authorization") String token);
}
