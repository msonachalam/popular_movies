package com.udacity.popularmovies;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieExecutor {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MovieExecutor sInstance;
    private final Executor diskIO;

    private MovieExecutor(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static MovieExecutor getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
