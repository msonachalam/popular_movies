package com.udacity.popularmovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDetails {

    @SerializedName("results")
    private List<MovieItem> moviesItemList = null;

    public List<MovieItem> getMoviesItemList() {
        return moviesItemList;
    }

    public void setMoviesItemList(List<MovieItem> moviesItemList) {
        this.moviesItemList = moviesItemList;
    }

    @Entity(tableName = "moviesDetail")
    public static class MovieItem {

        @Ignore
        @SerializedName("vote_count")
        private int voteCount;

        @PrimaryKey
        @SerializedName("id")
        private int id;

        @Ignore
        private boolean video;

        @ColumnInfo(name = "voteAverage")
        @SerializedName("vote_average")
        private double voteAverage;

        private String title;

        @Ignore
        @SerializedName("popularity")
        @Expose
        private double popularity;

        @ColumnInfo(name = "posterPath")
        @SerializedName("poster_path")
        private String posterPath;

        @Ignore
        @SerializedName("original_language")
        private String originalLanguage;

        @Ignore
        @SerializedName("original_title")
        private String originalTitle;

        @SerializedName("backdrop_path")
        @Expose
        private String backdropPath;

        @SerializedName("overview")
        private String overview;

        @ColumnInfo(name = "releaseDate")
        @SerializedName("release_date")
        private String releaseDate;

        @ColumnInfo(name = "favorite")
        @Expose(serialize = false, deserialize = false)
        private boolean isFavorite;

        public boolean isFavorite() {
            return isFavorite;
        }

        public void setFavorite(boolean favorite) {
            isFavorite = favorite;
        }

        @ColumnInfo(name = "type")
        @Expose(serialize = false, deserialize = false)
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isVideo() {
            return video;
        }

        public void setVideo(boolean video) {
            this.video = video;
        }

        public double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getPopularity() {
            return popularity;
        }

        public void setPopularity(double popularity) {
            this.popularity = popularity;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public void setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public void setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }
    }
}