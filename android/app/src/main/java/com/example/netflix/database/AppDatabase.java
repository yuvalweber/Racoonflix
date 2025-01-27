package com.example.netflix.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.netflix.dao.CategoryDao;
import com.example.netflix.dao.TokenDao;
import com.example.netflix.entities.CategoryEntity;
import com.example.netflix.entities.TokenEntity;

@Database(entities = {TokenEntity.class, CategoryEntity.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract TokenDao tokenDao();
    public abstract CategoryDao categoryDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "netflix_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
