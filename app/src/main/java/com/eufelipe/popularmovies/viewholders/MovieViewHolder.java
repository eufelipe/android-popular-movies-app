package com.eufelipe.popularmovies.viewholders;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView mPosterImageView;
    Context mContext;

    public MovieViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
        mPosterImageView = (ImageView) itemView.findViewById(R.id.poster);
    }

    public void bind(Movie movie) {
        Picasso.with(mContext)
                .load(movie.getPosterImage())
                .into(mPosterImageView);
    }


    @Override
    public void onClick(View view) {
        Toast.makeText(mContext, "Posição: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
    }
}
