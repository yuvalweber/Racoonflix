package com.example.netflix.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netflix.database.AppDatabase;
import com.example.netflix.entities.TokenEntity;
import com.example.netflix.models.Category;
import com.example.netflix.api.CategoryServiceApi;
import com.example.netflix.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {

    private final CategoryServiceApi categoryApi;
    private final AppDatabase appDatabase;

    public CategoryRepository(Context context) {
        categoryApi = RetrofitInstance.getInstance().create(CategoryServiceApi.class);
        appDatabase = AppDatabase.getInstance(context);
    }

    public LiveData<List<Category>> fetchCategories() {
        MutableLiveData<List<Category>> categoriesLiveData = new MutableLiveData<>();

        // Retrieve token from Room database
        new Thread(() -> {
            TokenEntity tokenEntity = appDatabase.tokenDao().getTokenData();
            if (tokenEntity != null) {
                String token = "Bearer " + tokenEntity.getToken();
                categoryApi.getCategories(token).enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            categoriesLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        }).start();

        return categoriesLiveData;
    }
}
