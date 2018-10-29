package com.udacity.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.udacity.popularmovies.MovieExecutor;
import com.udacity.popularmovies.database.MovieDataSource;
import com.udacity.popularmovies.database.MovieDatabase;
import com.udacity.popularmovies.database.MovieRepository;
import com.udacity.popularmovies.model.MovieDetails;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    public void setOrder(String orderChange) {
        order.setValue(orderChange);
    }

    private MutableLiveData<String> order = new MutableLiveData<>();

    public LiveData<List<MovieDetails.MovieItem>> getMovieListLiveData() {
        movieRepository = MovieRepository.getInstance(MovieDatabase.getDatabase(getApplication()).movieDao(),
                MovieDataSource.getInstance(), MovieExecutor.getInstance());
        return movieRepository.getMovieList(order);
    }

    private final MutableLiveData<List<MovieDetails.MovieItem>> mMovieList = new MutableLiveData();

    public MovieViewModel(@NonNull Application application) {
        super(application);
    }
}
