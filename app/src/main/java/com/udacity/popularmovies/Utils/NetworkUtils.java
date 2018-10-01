package com.udacity.popularmovies.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the Movie DB APIs
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    final static String MOVIE_DB_URL = "https://api.themoviedb.org/3/movie/";
    final static String API_KEY = "api_key";
    final static String API_VALUE = "03f68da16d33c51d1eb72cb513ad4c11";
    final static String THE_MOVIE_DB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    final static String THE_MOVIE_DB_FILE_SIZE = "w500";
    final static String PAGE_KEY = "page";
    final static String PAGE_VALUE = "1";


    public static URL buildUrl(String sortBy) {

        Uri builtUri = Uri.parse(MOVIE_DB_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(API_KEY, API_VALUE)
                .appendQueryParameter(PAGE_KEY, PAGE_VALUE)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI:" + url);

        return url;
    }

    public static String getImageUrl(String posterUrl) {
        return THE_MOVIE_DB_IMAGE_BASE_URL + THE_MOVIE_DB_FILE_SIZE + posterUrl;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param movieRequestUrl The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */

    public static String getResponseFromHttpUrl(URL movieRequestUrl) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) movieRequestUrl.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
