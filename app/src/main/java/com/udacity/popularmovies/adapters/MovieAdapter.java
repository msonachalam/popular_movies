package com.udacity.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.MovieDetails;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    public static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private final Context mContext;
    private ItemOnClickHandler onClickHandler;
    private List<MovieDetails.MovieItem> movieItems;

    public MovieAdapter(Context mContext, ItemOnClickHandler movieAdapterOnClickHandler) {
        this.mContext = mContext;
        this.onClickHandler = movieAdapterOnClickHandler;
    }

    public interface ItemOnClickHandler {
        void onClick(MovieDetails.MovieItem movie);
    }

    @NonNull
    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_row, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieAdapterViewHolder holder, int position) {
        String url = BASE_IMAGE_URL + movieItems.get(holder.getAdapterPosition()).getBackdropPath();
        Picasso.with(mContext).load(url).placeholder(R.drawable.ic_launcher_foreground).into(holder.iconMovieView);
    }

    @Override
    public int getItemCount() {
        if (null == movieItems) return 0;
        return movieItems.size();
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView iconMovieView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            this.iconMovieView = itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickHandler.onClick(movieItems.get(getAdapterPosition()));
        }
    }

    public void setMovieData(List<MovieDetails.MovieItem> movieData) {
        this.movieItems = movieData;
        notifyDataSetChanged();
    }
}