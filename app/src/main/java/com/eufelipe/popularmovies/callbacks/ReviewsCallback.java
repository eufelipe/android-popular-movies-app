package com.eufelipe.popularmovies.callbacks;


import com.eufelipe.popularmovies.models.MovieReview;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsCallback {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<MovieReview> results;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public ReviewsCallback setPage(int page) {
        this.page = page;
        return this;
    }

    public List<MovieReview> getResults() {
        return results;
    }

    public ReviewsCallback setResults(List<MovieReview> results) {
        this.results = results;
        return this;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public ReviewsCallback setTotalResults(int totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public ReviewsCallback setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }
}
