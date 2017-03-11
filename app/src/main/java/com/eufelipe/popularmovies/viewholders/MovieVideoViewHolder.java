package com.eufelipe.popularmovies.viewholders;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.activities.MovieActivity;
import com.eufelipe.popularmovies.application.Constants;
import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.models.MovieVideo;
import com.squareup.picasso.Picasso;

public class MovieVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView mThumbImageView;
    Context mContext;
    MovieVideo mMovieVideo;

    public MovieVideoViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
        mThumbImageView = (ImageView) itemView.findViewById(R.id.thumbnail);
    }

    public void bind(MovieVideo movieVideo) {
        mMovieVideo = movieVideo;
        Picasso.with(mContext)
                .load(movieVideo.getImage())
                .error(R.mipmap.ic_movie_thumb)
                .placeholder(R.mipmap.ic_movie_thumb)
                .into(mThumbImageView);
    }


    @Override
    public void onClick(View view) {

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + mMovieVideo.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + mMovieVideo.getKey()));
        try {
            mContext.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            mContext.startActivity(webIntent);
        }
    }
}
