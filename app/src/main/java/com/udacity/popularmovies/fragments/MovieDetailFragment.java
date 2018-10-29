package com.udacity.popularmovies.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.adapters.ReviewsAdapter;
import com.udacity.popularmovies.adapters.TrailerAdapter;
import com.udacity.popularmovies.viewmodel.MovieDetailViewModel;

import timber.log.Timber;

public class MovieDetailFragment extends Fragment implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String MOVIE_ITEM_ID = "movie_item_id";

    public static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    public static String BASE_IMAGE_HD_URL = "http://image.tmdb.org/t/p/w500/";
    public static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    public static final String DEFAULT_RATING = "10";

    private static final String TAG = MovieDetailFragment.class.getSimpleName();

    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewRecyclerView;

    private TextView trailerTextView, reviewTextView;
    private CheckBox favoriteCheckBox;

    private TrailerAdapter trailerAdapter;
    private ReviewsAdapter reviewsAdapter;

    private MovieDetailViewModel movieDetailViewModel;
    private int movieItemId;


    public MovieDetailFragment() {
    }

    public static MovieDetailFragment newInstance(int movieItemId) {

        Bundle args = new Bundle();
        args.putInt(MOVIE_ITEM_ID, movieItemId);

        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(MOVIE_ITEM_ID)) {
            movieItemId = getArguments().getInt(MOVIE_ITEM_ID);
        }
        movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        trailerRecyclerView = rootView.findViewById(R.id.rv_trailer);
        reviewRecyclerView = rootView.findViewById(R.id.rv_review);

        setupTrailerRecyclerView();
        setupReviewsRecyclerView();
        handleBaseView(rootView);
        updateFavorite();
        return rootView;
    }

    private void updateFavorite() {
        favoriteCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            movieDetailViewModel.setAsFavorite(movieItemId, isChecked);
        });
    }

    private void handleBaseView(View rootView) {
        trailerTextView = rootView.findViewById(R.id.tv_trailer);
        trailerTextView.setText(R.string.trailers);
        reviewTextView = rootView.findViewById(R.id.tv_review);
        reviewTextView.setText(R.string.reviews);
        favoriteCheckBox = rootView.findViewById(R.id.cb_favorite);
        if (movieItemId != -1) {
            movieDetailViewModel.getMovieItem(movieItemId).observe(this, movie -> {
                if (movie != null) {
                    String url = BASE_IMAGE_HD_URL + movie.getPosterPath();
                    Picasso.with(getActivity()).load(url).placeholder(R.drawable.ic_launcher_foreground)
                            .into((ImageView) rootView.findViewById(R.id.iv_poster));

                    url = BASE_IMAGE_URL + movie.getBackdropPath();
                    Picasso.with(getActivity()).load(url).placeholder(R.drawable.ic_launcher_foreground)
                            .into((ImageView) rootView.findViewById(R.id.iv_backdropPath));
                    ((TextView) rootView.findViewById(R.id.tv_releasedate)).setText(movie.getReleaseDate());
                    ((TextView) rootView.findViewById(R.id.tv_rating)).setText(String.valueOf(movie.getVoteAverage()) + "/" + DEFAULT_RATING);
                    ((TextView) rootView.findViewById(R.id.tv_overview)).setText(movie.getOverview());
                    favoriteCheckBox.setChecked(movie.isFavorite());
                }
            });
        }
    }

    private void setupTrailerRecyclerView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        trailerRecyclerView.setLayoutManager(layoutManager);
        trailerRecyclerView.setHasFixedSize(true);
        trailerRecyclerView.addItemDecoration(new DividerItemDecoration(trailerRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        trailerAdapter = new TrailerAdapter(getActivity(), this);
        trailerRecyclerView.setAdapter(trailerAdapter);
        movieDetailViewModel.getTrailers(movieItemId).observe(this, trailerItems -> {
            trailerAdapter.setTrailerItemList(trailerItems);
            if (trailerItems != null && trailerItems.size() > 0) {
                trailerTextView.setVisibility(View.VISIBLE);
                trailerRecyclerView.setVisibility(View.VISIBLE);
            } else {
                trailerTextView.setVisibility(View.GONE);
                trailerRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void setupReviewsRecyclerView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        reviewRecyclerView.setLayoutManager(layoutManager);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.addItemDecoration(new DividerItemDecoration(reviewRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        reviewsAdapter = new ReviewsAdapter(getActivity());
        reviewRecyclerView.setAdapter(reviewsAdapter);
        movieDetailViewModel.getReviews(movieItemId).observe(this, reviewItems -> {
            reviewsAdapter.setReviewsList(reviewItems);
            if (reviewItems != null && reviewItems.size() > 0) {
                reviewTextView.setVisibility(View.VISIBLE);
                reviewRecyclerView.setVisibility(View.VISIBLE);
            } else {
                reviewTextView.setVisibility(View.GONE);
                reviewRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(String key) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL + key));
        PackageManager packageManager = getActivity().getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
        } else {
            Timber.d(TAG, "Intent not available");
        }
    }
}