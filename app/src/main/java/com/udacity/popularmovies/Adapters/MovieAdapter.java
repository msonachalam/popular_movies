package com.udacity.popularmovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.Model.Movie;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.Utils.NetworkUtils;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final String TAG = MovieAdapter.class.getSimpleName();

    private Context mcontext;
    private List<Movie> popularMoviesList;
    private final ItemOnClickHandler itemOnClickHandler;

    public MovieAdapter(Context context, ItemOnClickHandler clickHandler) {
        mcontext = context;
        itemOnClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = popularMoviesList.get(position);

        Picasso.with(mcontext)
                .load(NetworkUtils.getImageUrl(movie.getImage()))
                .into(holder.getMovieListImageItem());
    }

    @Override
    public int getItemCount() {
        if (popularMoviesList == null || popularMoviesList.isEmpty()) return 0;
        return popularMoviesList.size();
    }

    public void setMovieData(List<Movie> movieData) {
        popularMoviesList = movieData;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView posterView;

        public ImageView getMovieListImageItem() {
            return posterView;
        }

        public MovieViewHolder(View itemView) {
            super(itemView);
            posterView = itemView.findViewById(R.id.movie_item_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemOnClickHandler.onClick(popularMoviesList.get(getAdapterPosition()));
        }
    }

    public interface ItemOnClickHandler {
        void onClick(Movie movie);
    }
}