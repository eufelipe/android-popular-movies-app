package com.eufelipe.popularmovies.viewholders;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.activities.MovieActivity;
import com.eufelipe.popularmovies.application.Constants;
import com.eufelipe.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView mPosterImageView;
    Context mContext;
    Movie mMovie;

    public MovieViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
        mPosterImageView = (ImageView) itemView.findViewById(R.id.poster);
    }

    public void bind(Movie movie) {
        mMovie = movie;
        Picasso.with(mContext)
                .load(movie.getPosterImage())
                .error(R.mipmap.ic_movie_thumb)
                .placeholder(R.mipmap.ic_movie_thumb)
                .into(mPosterImageView);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, MovieActivity.class);
        intent.putExtra(Constants.MOVIE_DATA_KEY, mMovie);
        mContext.startActivity(intent);
    }
}
