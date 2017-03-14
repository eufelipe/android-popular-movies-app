package com.eufelipe.popularmovies.callbacks;


import com.eufelipe.popularmovies.models.MovieVideo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosCallback {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<MovieVideo> results;

    public int getId() {
        return id;
    }

    public VideosCallback setId(int id) {
        this.id = id;
        return this;
    }

    public List<MovieVideo> getResults() {
        return results;
    }

    public VideosCallback setResults(List<MovieVideo> results) {
        this.results = results;
        return this;
    }
}
