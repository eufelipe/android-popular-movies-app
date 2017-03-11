package com.eufelipe.popularmovies.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.models.MovieReview;
import com.eufelipe.popularmovies.viewholders.MovieReviewViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewViewHolder> {

    private List<MovieReview> movieReviews = new ArrayList<>();


    public MovieReviewAdapter(List<MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View layout = layoutInflater.inflate(R.layout.fragment_movie_review_item, null);
        return new MovieReviewViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        MovieReview movieReview = movieReviews.get(position);
        holder.bind(movieReview);
    }

    @Override
    public int getItemCount() {
        return this.movieReviews.size();
    }

}
