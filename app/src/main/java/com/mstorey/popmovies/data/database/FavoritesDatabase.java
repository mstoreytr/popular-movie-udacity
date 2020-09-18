package com.mstorey.popmovies.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mstorey.popmovies.data.responses.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class FavoritesDatabase extends RoomDatabase {
    public abstract FavoritesDao favoritesDao();
    // volatile is used for handling multi-threads
    private volatile static FavoritesDatabase INSTANCE;

    public static FavoritesDatabase getInstance(Context context) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (FavoritesDatabase.class) {
            // Make sure inside of thread that instance is still null
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FavoritesDatabase.class, "favorites").build();
            }
        }
        return INSTANCE;
    }

}
