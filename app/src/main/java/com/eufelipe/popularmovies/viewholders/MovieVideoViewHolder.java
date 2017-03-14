package com.eufelipe.popularmovies.viewholders;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.bases.BaseViewHolder;
import com.eufelipe.popularmovies.helpers.NetworkHelper;
import com.eufelipe.popularmovies.models.MovieVideo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieVideoViewHolder extends BaseViewHolder implements View.OnClickListener {

    @BindView(R.id.thumbnail)
    ImageView mThumbImageView;

    private final Context mContext;
    private MovieVideo mMovieVideo;

    public MovieVideoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bind(MovieVideo movieVideo) {
        mMovieVideo = movieVideo;
        setImage(mContext, movieVideo.getImage(), mThumbImageView);
    }


    @Override
    public void onClick(View view) {
        NetworkHelper.openVideoFromYoutube(mMovieVideo.getKey(), mContext);
    }
}
