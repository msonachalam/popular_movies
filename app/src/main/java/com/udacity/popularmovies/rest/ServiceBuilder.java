package com.udacity.popularmovies.rest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {
    private static String MOVIE_DB_URL = "https://api.themoviedb.org/3/";

    private static OkHttpClient.Builder okHttp =
            new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        Request request = chain.request();

                        request = request.newBuilder()
                                .build();

                        return chain.proceed(request);
                    });

    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(MOVIE_DB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build());

    private static Retrofit retrofit = builder.build();

    public static <S> S buildService(Class<S> serviceType) {
        return retrofit.create(serviceType);
    }

}
