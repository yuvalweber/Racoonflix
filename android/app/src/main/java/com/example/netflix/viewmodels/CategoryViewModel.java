package com.example.netflix.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.netflix.models.Category;
import com.example.netflix.repository.CategoryRepository;

import retrofit2.Callback;

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

    public void createCategory(Category category, Callback<Void> callback) {
        categoryRepository.createCategory(category, callback);
    }

    public void updateCategory(String id, Category category, Callback<Void> callback) {
        categoryRepository.updateCategory(id, category, callback);
    }

    public void deleteCategory(String id, Callback<Void> callback) {
        categoryRepository.deleteCategory(id, callback);
    }
    public void fetchCategoryIdByName(String categoryName, Callback<List<Category>> callback) {
        categoryRepository.fetchCategoryIdByName(categoryName, callback);
    }

    public List<Category> fetchCategoriesIdByName(String [] categoryNames) {
        return categoryRepository.fetchCategoriesIdByNameSync(categoryNames);
    }

}
