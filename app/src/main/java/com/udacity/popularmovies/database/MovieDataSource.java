package com.udacity.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.udacity.popularmovies.BuildConfig;
import com.udacity.popularmovies.model.MovieDetails;
import com.udacity.popularmovies.model.Reviews;
import com.udacity.popularmovies.model.Trailers;
import com.udacity.popularmovies.rest.ApiClient;
import com.udacity.popularmovies.rest.ServiceBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MovieDataSource {

    private static final String TAG = MovieDataSource.class.getSimpleName();
    public static final String FAVORITE = "favorite";

    private static MovieDataSource instance;
    private static final Object LOCK = new Object();

    private MutableLiveData<List<MovieDetails.MovieItem>> movieListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Trailers.TrailerItem>> trailerList = new MutableLiveData<>();
    private MutableLiveData<List<Reviews.ReviewItem>> reviewsList = new MutableLiveData<>();

    public static MovieDataSource getInstance() {
        Timber.d(TAG, "Getting the network data source");
        if (instance == null) {
            synchronized (LOCK) {
                instance = new MovieDataSource();
                Timber.d(TAG, "Made new network data source");
            }
        }
        return instance;
    }

    public MutableLiveData<List<MovieDetails.MovieItem>> getMovieList(final String sortBy) {
        Timber.d(TAG, "Get movie details: %s", sortBy);
        if (!FAVORITE.equalsIgnoreCase(sortBy)) {
            ApiClient apiClient = ServiceBuilder.buildService(ApiClient.class);

            Call<MovieDetails> movieDetailsCall;
            movieDetailsCall = apiClient.getMovieDetails(sortBy, BuildConfig.API_KEY);
            movieDetailsCall.enqueue(new Callback<MovieDetails>() {
                @Override
                public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                    MovieDetails movieDetails = response.body();
                    Timber.d(TAG, "Response: %s", response.body());

                    if (movieDetails != null && movieDetails.getMoviesItemList().size() > 0) {
                        Timber.d(TAG, "onResponse size: %d", movieDetails.getMoviesItemList().size());
                        List<MovieDetails.MovieItem> movieList = movieDetails.getMoviesItemList();
                        for (MovieDetails.MovieItem movieItem : movieList) {
                            movieItem.setType(sortBy);
                        }
                        movieListLiveData.postValue(movieDetails.getMoviesItemList());
                    }
                }

                @Override
                public void onFailure(Call<MovieDetails> call, Throwable t) {

                }
            });
        } else {
            Timber.d(TAG, "getMovieDetails: %s", sortBy);
            movieListLiveData.setValue(new MovieDetails().getMoviesItemList());
        }
        return movieListLiveData;
    }

    public LiveData<List<Trailers.TrailerItem>> getTrailers(int movieId) {
        ApiClient apiClient = ServiceBuilder.buildService(ApiClient.class);
        Call<Trailers> trailersCall;

        trailersCall = apiClient.getTrailers(movieId, BuildConfig.API_KEY);
        trailersCall.enqueue(new Callback<Trailers>() {
            @Override
            public void onResponse(Call<Trailers> call, Response<Trailers> response) {
                if (response.body() != null) {
                    Timber.d(TAG, "Trailer Response:%s", response.body().getResults());
                    trailerList.postValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<Trailers> call, Throwable t) {

            }
        });
        return trailerList;
    }

    public LiveData<List<Reviews.ReviewItem>> getReviews(int movieId) {
        ApiClient apiClient = ServiceBuilder.buildService(ApiClient.class);
        Call<Reviews> trailersCall;

        trailersCall = apiClient.getReviews(movieId, BuildConfig.API_KEY);
        trailersCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                if (response.body() != null) {
                    Timber.d(TAG, "Reviews response:%s", response.body().getResults());
                    reviewsList.postValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {

            }
        });
        return reviewsList;
    }
}
