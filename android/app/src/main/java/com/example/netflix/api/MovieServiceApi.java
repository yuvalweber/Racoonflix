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
	// Create tag for logging
	static final String TAG = "MovieServiceApi";

	// Endpoint to get a list of movies
	@GET("/api/movies")
	Call<List<Movie>> getMovies(@Header("Authorization") String token);

	// Endpoint to get all movies
	@GET("/api/movies/all")
	Call<List<Movie>> getAllMovies(@Header("Authorization") String token);

	// Endpoint to search movies by query
	@GET("/api/movies/search/{query}")
	Call<List<Movie>> searchMovies(@Header("Authorization") String token, @Path("query") String query);

	// Endpoint to create a movie with files (image and trailer)
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

	// Endpoint to update a movie by ID
	@PUT("/api/movies/{id}")
	Call<Void> updateMovie(@Header("Authorization") String token, @Path("id") String id, @Body Movie movie);

	// Endpoint to delete a movie by ID
	@DELETE("/api/movies/{id}")
	Call<Void> deleteMovie(@Header("Authorization") String token, @Path("id") String id);

	// Endpoint to get recommended movies based on a movie ID
	@GET("/api/movies/{id}/recommend")
	Call<List<Movie>> getRecommendedMovies(@Header("Authorization") String token, @Path("id") String movieId);

	// Endpoint to add a movie to recommendations based on a movie ID
	@POST("/api/movies/{id}/recommend")
	Call<Void> addMovieToRecommendations(@Header("Authorization") String token, @Path("id") String movieId);
}
