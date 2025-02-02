package com.example.netflix.network;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
	// Base URL for the API
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://localhost:8080";
	// Singleton pattern to create a single instance of the Retrofit object
    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
