package com.udacity.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Reviews;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {

    private final Context mContext;

    public void setReviewsList(List<Reviews.ReviewItem> reviewsList) {
        this.reviewsList = reviewsList;
        notifyDataSetChanged();
    }

    private List<Reviews.ReviewItem> reviewsList;

    public ReviewsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_review_row, parent, false);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapterViewHolder holder, int position) {
        holder.author.setText(reviewsList.get(holder.getAdapterPosition()).getAuthor());
        holder.content.setText(reviewsList.get(holder.getAdapterPosition()).getContent());
    }

    @Override
    public int getItemCount() {
        if (null == reviewsList) return 0;
        return reviewsList.size();
    }


    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView author, content;

        public ReviewsAdapterViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.tv_author);
            content = itemView.findViewById(R.id.tv_content);
        }
    }
}
