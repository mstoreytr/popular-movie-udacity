package com.mstorey.popmovies.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mstorey.popmovies.data.responses.Movie;

import java.util.List;

@Dao
public interface FavoritesDao {

    @Query("SELECT * FROM favorites")
    LiveData<List<Movie>> getFavorites();

    @Insert
    void insertFavorite(Movie movie);

    @Delete
    void deleteFavorite(Movie movie);

    @Query("SELECT * FROM favorites WHERE id = :movieID")
    LiveData<Movie> getFavorite(int movieID);
}
