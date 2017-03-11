package com.eufelipe.popularmovies.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.models.MovieVideo;
import com.eufelipe.popularmovies.viewholders.MovieVideoViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoViewHolder> {

    private List<MovieVideo> movieVideos = new ArrayList<>();


    public MovieVideoAdapter(List<MovieVideo> movieVideos) {
        this.movieVideos = movieVideos;
    }

    @Override
    public MovieVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View layout = layoutInflater.inflate(R.layout.fragment_movie_videos_item, null);
        return new MovieVideoViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(MovieVideoViewHolder holder, int position) {
        MovieVideo movieVideo = movieVideos.get(position);
        holder.bind(movieVideo);
    }

    @Override
    public int getItemCount() {
        return this.movieVideos.size();
    }


}
