package com.example.netflix.api;

import com.example.netflix.models.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Header;

public interface MovieServiceApi {
    @GET("/api/movies")
    Call<List<Movie>> getMovies(@Header("Authorization") String token);

    @GET("/api/movies/all")
    Call<List<Movie>> getAllMovies(@Header("Authorization") String token);

    @GET("/api/movies/search/{query}")
    Call<List<Movie>> searchMovies(@Header("Authorization") String token, @Path("query") String query);
}
