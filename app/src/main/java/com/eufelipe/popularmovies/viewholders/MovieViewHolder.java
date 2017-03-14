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
import com.eufelipe.popularmovies.bases.BaseViewHolder;
import com.eufelipe.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieViewHolder extends BaseViewHolder implements View.OnClickListener {

    @BindView(R.id.poster)
    ImageView mPosterImageView;

    private final Context mContext;
    private Movie mMovie;

    public MovieViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bind(Movie movie) {
        mMovie = movie;
        setImage(mContext, movie.getPosterImage(), mPosterImageView);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, MovieActivity.class);
        intent.putExtra(Constants.MOVIE_DATA_KEY, mMovie);
        mContext.startActivity(intent);
    }
}
