package com.udacity.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.udacity.popularmovies.model.MovieDetails;

import timber.log.Timber;

@Database(entities = {MovieDetails.MovieItem.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String TAG = MovieDatabase.class.getSimpleName();
    private static MovieDatabase INSTANCE;
    private static final Object LOCK = new Object();

    public static MovieDatabase getDatabase(Context context) {

        if (INSTANCE == null) {
            synchronized (LOCK) {
                Timber.d(TAG, "Creating new database instance");
                INSTANCE = Room.databaseBuilder(context,
                        MovieDatabase.class, "favoriteMovies")
                        .build();
                Timber.d(TAG, "Database created");
            }
        }
        return INSTANCE;
    }

    public abstract MovieDao movieDao();
}
