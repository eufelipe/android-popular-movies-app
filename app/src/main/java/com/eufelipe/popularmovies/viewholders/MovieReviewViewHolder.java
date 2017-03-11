package com.eufelipe.popularmovies.viewholders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.models.MovieReview;

public class MovieReviewViewHolder extends RecyclerView.ViewHolder {

    TextView mAuthorTextView;
    TextView mContentTextView;

    public MovieReviewViewHolder(View itemView) {
        super(itemView);
        mAuthorTextView = (TextView) itemView.findViewById(R.id.tv_author);
        mContentTextView = (TextView) itemView.findViewById(R.id.tv_content);
    }

    public void bind(MovieReview movieReview) {
        mAuthorTextView.setText(movieReview.getAuthor());
        mContentTextView.setText(movieReview.getContent());
    }
}
