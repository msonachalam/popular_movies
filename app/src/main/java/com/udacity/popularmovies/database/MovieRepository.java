package com.udacity.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.udacity.popularmovies.MovieExecutor;
import com.udacity.popularmovies.model.MovieDetails;
import com.udacity.popularmovies.model.Reviews;
import com.udacity.popularmovies.model.Trailers;

import java.util.List;

import timber.log.Timber;

import static com.udacity.popularmovies.database.MovieDataSource.FAVORITE;

public class MovieRepository {
    private static final String TAG = MovieRepository.class.getSimpleName();

    private final MovieExecutor mExecutors;
    private final MovieDao mMovieDetailDao;
    private final MovieDataSource mMovieNetworkDataSource;

    private LiveData<List<MovieDetails.MovieItem>> movieListLiveData;
    private MutableLiveData<MovieDetails.MovieItem> movieItemMutableLiveData = new MutableLiveData<>();

    public LiveData<List<MovieDetails.MovieItem>> getMovieList(LiveData<String> sortType) {
        LiveData<List<MovieDetails.MovieItem>> mNetworkMovieDetailsLiveData =
                Transformations.switchMap(sortType, sortTypeChange -> mMovieNetworkDataSource.getMovieList(sortTypeChange));

        movieListLiveData = Transformations.switchMap(mNetworkMovieDetailsLiveData, movieDetailsLiveDataChange -> {
            if (!sortType.getValue().equalsIgnoreCase(FAVORITE)) {
                mExecutors.diskIO().execute(() -> {
                    mMovieDetailDao.deleteMovieItems(sortType.getValue());
                    mMovieDetailDao.insertAllMovieDetails(movieDetailsLiveDataChange);
                });
            } else {
                return mMovieDetailDao.getFavoriteMovieList();
            }
            return mMovieDetailDao.getMovieList(sortType.getValue());
        });

        return movieListLiveData;
    }

    private static final Object LOCK = new Object();
    private static MovieRepository sInstance;

    private MovieRepository(MovieDao movieDetailDao, MovieDataSource movieNetworkDataSource,
                            MovieExecutor executors) {
        mMovieDetailDao = movieDetailDao;
        mMovieNetworkDataSource = movieNetworkDataSource;
        mExecutors = executors;

    }

    public synchronized static MovieRepository getInstance(MovieDao movieDetailDao,
                                                           MovieDataSource movieDataSource, MovieExecutor executors) {
        Timber.d(TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieRepository(movieDetailDao, movieDataSource,
                        executors);
            }
        }
        return sInstance;
    }

    public LiveData<MovieDetails.MovieItem> getMovieDetailsItem(int movieId) {
        mExecutors.diskIO().execute(() -> {
            MovieDetails.MovieItem movieItem = mMovieDetailDao.getMovieItemDetails(movieId);
            movieItemMutableLiveData.postValue(movieItem);
        });
        return movieItemMutableLiveData;
    }

    public LiveData<List<Trailers.TrailerItem>> getTrailers(int movieId) {
        return mMovieNetworkDataSource.getTrailers(movieId);
    }

    public LiveData<List<Reviews.ReviewItem>> getReviews(int movieId) {
        return mMovieNetworkDataSource.getReviews(movieId);
    }

    public void setAsFavorite(int mMovieItemId, boolean isChecked) {
        mExecutors.diskIO().execute(() -> mMovieDetailDao.updateMovieAsFavorite(mMovieItemId, isChecked));
    }
}
