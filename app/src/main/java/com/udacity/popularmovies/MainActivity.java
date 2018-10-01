package com.udacity.popularmovies;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.udacity.popularmovies.Adapters.MovieAdapter;
import com.udacity.popularmovies.Model.Movie;
import com.udacity.popularmovies.Utils.JsonHelper;
import com.udacity.popularmovies.Utils.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemOnClickHandler {

    private RecyclerView movieListView;
    private MovieAdapter movieAdapter;
    private ProgressDialog progressIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieListView = findViewById(R.id.movies_recycler_view);
        movieListView.setLayoutManager(new GridLayoutManager(this, 3, 1, false));
        movieListView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(this, this);
        movieListView.setAdapter(movieAdapter);

        loadMovies("popular");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.movie_sort_most_popular:
                loadMovies("popular");
                return true;
            case R.id.movie_sort_top_rated:
                loadMovies("top_rated");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovies(String sortBy) {
        new MovieFetcherTask().execute(sortBy);
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieDetails.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        String movieAsJson = new Gson().toJson(movie);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movieAsJson);
        startActivity(intentToStartDetailActivity);
        Toast.makeText(this, "Viewing " + movie.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private class MovieFetcherTask extends AsyncTask<String, Void, List<Movie>> {

        private final String TAG = MovieFetcherTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressIndicator = ProgressDialog.show(MainActivity.this, "Wait...", "loading movies...", true);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            String queryPath = null;
            if (params.length == 0) {
                return null;
            }
            queryPath = params[0];

            URL movieRequestUrl = NetworkUtils.buildUrl(queryPath);
            try {

                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                Log.v(TAG, "Response Back: " + jsonMovieResponse);
                return JsonHelper.getPopularMovieStringsFromJson(jsonMovieResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            progressIndicator.dismiss();
            if (!movies.isEmpty()) {
                movieAdapter.setMovieData(movies);
            }
            super.onPostExecute(movies);
        }
    }
}
