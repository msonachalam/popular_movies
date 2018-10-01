package com.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.Model.Movie;
import com.udacity.popularmovies.Utils.NetworkUtils;

public class MovieDetails extends AppCompatActivity {

    private ImageView poster;
    private TextView title;
    private TextView releaseDate;
    private TextView voteAverage;
    private TextView overview;
    private RatingBar voterAverage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        poster = findViewById(R.id.movie_details);
        title = findViewById(R.id.title);
        releaseDate = findViewById(R.id.date_released);
        voteAverage = findViewById(R.id.vote_average);
        overview = findViewById(R.id.overview);
        voterAverage = findViewById(R.id.voter_average);


        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                String movieAsJson = intent.getStringExtra(Intent.EXTRA_TEXT);
                Movie moviePassed = new Gson().fromJson(movieAsJson, Movie.class);

                Picasso.with(this).load(NetworkUtils.getImageUrl(moviePassed.getImage())).into(poster);
                title.setText(moviePassed.getTitle());
                releaseDate.setText(moviePassed.getReleaseDate());
                voteAverage.setText(String.valueOf(moviePassed.getUserRating()));
                overview.setText(moviePassed.getOverview());
                voterAverage.setRating(moviePassed.getVoteAverage());
            }
        }
    }

    public void OnClickBackToList(View view) {
        Context context = this;
        Class destinationClass = MainActivity.class;
        Intent mainActivityIntent = new Intent(context, destinationClass);
        startActivity(mainActivityIntent);
    }
}
