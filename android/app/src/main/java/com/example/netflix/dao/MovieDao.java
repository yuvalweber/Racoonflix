package com.example.netflix.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.netflix.entities.MovieEntity;
import java.util.List;


@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<MovieEntity> movies);

    @Query("SELECT * FROM movies WHERE timestamp > :validTime")
    List<MovieEntity> getValidMovies(long validTime);

    @Query("DELETE FROM movies")
    void clearMovies();
}
