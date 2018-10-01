package com.udacity.popularmovies.Utils;

import android.util.Log;

import com.udacity.popularmovies.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JsonHelper {

    private final static String TAG = JsonHelper.class.getSimpleName();

    private final static String RESULTS = "results";
    private final static String TITLE = "title";
    private final static String IMAGE_PATH = "poster_path";
    private final static String OVERVIEW = "overview";
    private final static String RATING = "vote_average";
    private final static String RELEASE_DATE = "release_date";
    private final static String VOTE_AVERAGE = "vote_average";

    public static List<Movie> getPopularMovieStringsFromJson(String json) throws JSONException {

        List<Movie> popularMovieList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(json);
            JSONArray results = root.getJSONArray(RESULTS);

            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonObject = results.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setTitle(jsonObject.getString(TITLE));
                    movie.setUserRating(jsonObject.getString(RATING));
                    movie.setImage(jsonObject.getString(IMAGE_PATH));
                    movie.setOverview(jsonObject.getString(OVERVIEW));
                    float voterAvg = (float) jsonObject.getDouble(VOTE_AVERAGE);
                    movie.setVoteAverage(voterAvg);
                    movie.setReleaseDate(jsonObject.getString(RELEASE_DATE));
                    popularMovieList.add(movie);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return popularMovieList;
    }
}
