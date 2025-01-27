package com.example.netflix.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.netflix.entities.CategoryEntity;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategories(List<CategoryEntity> categories);

    @Query("SELECT * FROM categories WHERE timestamp > :validTime")
    List<CategoryEntity> getValidCategories(long validTime);

    @Query("DELETE FROM categories")
    void clearCategories();
}
