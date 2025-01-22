package com.example.netflix.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.netflix.models.Category;
import com.example.netflix.repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private final CategoryRepository categoryRepository;

    public CategoryViewModel(Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
    }

    public LiveData<List<Category>> getCategories() {
        return categoryRepository.fetchCategories();
    }
}
