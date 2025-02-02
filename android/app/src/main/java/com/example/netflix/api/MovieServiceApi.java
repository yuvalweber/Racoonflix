package com.example.netflix.api;

import com.example.netflix.models.Movie;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Header;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;


public interface MovieServiceApi {
    //create tag for logging
    static final String TAG = "MovieServiceApi";
    @GET("/api/movies")
    Call<List<Movie>> getMovies(@Header("Authorization") String token);

	//add a new endpoint to get all movies
	//add a new endpoint to get all movies
    @GET("/api/movies/all")
    Call<List<Movie>> getAllMovies(@Header("Authorization") String token);

	//add a new endpoint to search movies
	//add a new endpoint to search movies
    @GET("/api/movies/search/{query}")
    Call<List<Movie>> searchMovies(@Header("Authorization") String token, @Path("query") String query);

    @Multipart
    @POST("/api/movies")
    Call<Void> createMovieWithFiles(
            @Header("Authorization") String token,
            @Part("title") RequestBody title,
            @Part("year") RequestBody year,
            @Part("director") RequestBody director,
            @Part("duration") RequestBody duration,
            @Part List<MultipartBody.Part> categories, // Accept categories as an array
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part trailer
    );

    @PUT("/api/movies/{id}")
    Call<Void> updateMovie(@Header("Authorization") String token, @Path("id") String id, @Body Movie movie);

	//add a new endpoint to delete a movie
	//add a new endpoint to delete a movie
    @DELETE("/api/movies/{id}")
    Call<Void> deleteMovie(@Header("Authorization") String token, @Path("id") String id);

	//add a new endpoint to get recommended movies
	//add a new endpoint to get recommended movies
    @GET("/api/movies/{id}/recommend")
    Call<List<Movie>> getRecommendedMovies(@Header("Authorization") String token, @Path("id") String movieId);

	//add a new endpoint to add a movie to recommendations
	//add a new endpoint to add a movie to recommendations
    @POST("/api/movies/{id}/recommend")
    Call<Void> addMovieToRecommendations(@Header("Authorization") String token, @Path("id") String movieId);

}
