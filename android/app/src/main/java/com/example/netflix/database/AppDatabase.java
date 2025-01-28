package com.example.netflix.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.netflix.dao.CategoryDao;
import com.example.netflix.dao.MovieDao;
import com.example.netflix.dao.TokenDao;
import com.example.netflix.entities.CategoryEntity;
import com.example.netflix.entities.MovieEntity;
import com.example.netflix.entities.TokenEntity;

@Database(entities = {TokenEntity.class, CategoryEntity.class, MovieEntity.class}, version = 4, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract TokenDao tokenDao();
    public abstract CategoryDao categoryDao();

    public abstract MovieDao movieDao();

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
