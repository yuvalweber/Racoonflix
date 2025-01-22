package com.example.netflix.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.netflix.entities.TokenEntity;

@Dao
public interface TokenDao {
    @Insert
    void insertToken(TokenEntity tokenEntity);

    @Query("SELECT * FROM tokens LIMIT 1")
    TokenEntity getTokenData();

    @Query("DELETE FROM tokens")
    void clearTokens();
}
