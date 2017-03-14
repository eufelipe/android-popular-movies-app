package com.eufelipe.popularmovies.callbacks;


import com.eufelipe.popularmovies.models.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesCallback {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Movie> results;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public MoviesCallback setPage(int page) {
        this.page = page;
        return this;
    }

    public List<Movie> getResults() {
        return results;
    }

    public MoviesCallback setResults(List<Movie> results) {
        this.results = results;
        return this;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public MoviesCallback setTotalResults(int totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public MoviesCallback setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }
}
