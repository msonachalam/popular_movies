package com.udacity.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.udacity.popularmovies.MovieExecutor;
import com.udacity.popularmovies.database.MovieDataSource;
import com.udacity.popularmovies.database.MovieDatabase;
import com.udacity.popularmovies.database.MovieRepository;
import com.udacity.popularmovies.model.MovieDetails;
import com.udacity.popularmovies.model.Reviews;
import com.udacity.popularmovies.model.Trailers;

import java.util.List;

public class MovieDetailViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    public LiveData<MovieDetails.MovieItem> getMovieItem(int movieId) {
        return movieRepository.getMovieDetailsItem(movieId);
    }

    public LiveData<List<Trailers.TrailerItem>> getTrailers(int movieId) {
        return movieRepository.getTrailers(movieId);
    }

    public LiveData<List<Reviews.ReviewItem>> getReviews(int movieId) {
        return movieRepository.getReviews(movieId);
    }

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance(MovieDatabase.getDatabase(getApplication()).movieDao(),
                MovieDataSource.getInstance(), MovieExecutor.getInstance());
    }

    public void setAsFavorite(int mMovieItemId, boolean isChecked) {
        movieRepository.setAsFavorite(mMovieItemId, isChecked);
    }
}