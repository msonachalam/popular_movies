package com.udacity.popularmovies.model;

import java.util.List;

public class Reviews {

    private int id;
    private List<ReviewItem> results = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ReviewItem> getResults() {
        return results;
    }

    public class ReviewItem {

        private String author;
        private String content;
        private String id;
        private String url;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "ReviewItem{" +
                    "author='" + author + '\'' +
                    ", content='" + content + '\'' +
                    ", id='" + id + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
