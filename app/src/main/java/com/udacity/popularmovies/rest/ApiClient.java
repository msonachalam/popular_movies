package com.udacity.popularmovies.rest;

import com.udacity.popularmovies.model.MovieDetails;
import com.udacity.popularmovies.model.Reviews;
import com.udacity.popularmovies.model.Trailers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {

    @GET("movie/{sortBy}")
    Call<MovieDetails> getMovieDetails(@Path("sortBy") String sortBy, @Query("api_key") String apikey);

    @GET("movie/{id}/videos")
    Call<Trailers> getTrailers(@Path("id") int movieId, @Query("api_key") String apikey);

    @GET("movie/{id}/reviews")
    Call<Reviews> getReviews(@Path("id") int movieId, @Query("api_key") String apikey);
}
