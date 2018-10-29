package com.udacity.popularmovies;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.popularmovies.adapters.MovieAdapter;
import com.udacity.popularmovies.model.MovieDetails;
import com.udacity.popularmovies.viewmodel.MovieViewModel;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String STATE_KEY = "state_key";
    public static final String MOVIE_KEY = "movie_key";

    private MovieViewModel movieViewModel;
    private RecyclerView movieListView;
    private MovieAdapter movieAdapter;
    private String currentSortOrder;
    private Parcelable listSortState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null)
            listSortState = savedInstanceState.getParcelable(STATE_KEY);

        currentSortOrder = PreferenceManager.getDefaultSharedPreferences(this).
                getString(getResources().getString(R.string.sort_order), getResources().getString(R.string.popular));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getTitle() + "(" + currentSortOrder + ")");

        movieListView = findViewById(R.id.movie_list);
        movieListView.setLayoutManager(new GridLayoutManager(getApplicationContext(), calculateNoOfColumns()));
        movieListView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this, this);
        movieListView.setAdapter(movieAdapter);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.setOrder(currentSortOrder);
        movieViewModel.getMovieListLiveData().observe(this, movieList -> {
            if (movieList != null) {
                movieAdapter.setMovieData(movieList);

                if (listSortState != null)
                    movieListView.getLayoutManager().onRestoreInstanceState(listSortState);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_KEY, movieListView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String persistedSortOrder = sharedPreferences.getString(getResources().getString(R.string.sortorder), getResources().getString(R.string.popular));
        if (!isNetworkAvailable()) {
            Toast.makeText(this, getResources().getString(R.string.no_network), Toast.LENGTH_SHORT).show();
        }
        if (isNetworkAvailable() && !currentSortOrder.equalsIgnoreCase(persistedSortOrder)) {
            currentSortOrder = persistedSortOrder;
            Timber.d(TAG, "onResume: sort %s", currentSortOrder);
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(getTitle() + "(" + currentSortOrder + ")");
            movieViewModel.setOrder(currentSortOrder);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, PreferenceActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(MovieDetails.MovieItem movie) {

        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_KEY, movie.getId());
        startActivity(intent);
        Toast.makeText(this, "Viewing " + movie.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
