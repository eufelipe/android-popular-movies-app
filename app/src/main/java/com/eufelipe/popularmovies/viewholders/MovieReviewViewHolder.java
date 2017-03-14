package com.eufelipe.popularmovies.viewholders;


import android.view.View;
import android.widget.TextView;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.bases.BaseViewHolder;
import com.eufelipe.popularmovies.models.MovieReview;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewViewHolder extends BaseViewHolder {

    @BindView(R.id.tv_author)
    TextView mAuthorTextView;

    @BindView(R.id.tv_content)
    TextView mContentTextView;

    public MovieReviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(MovieReview movieReview) {
        setText(mAuthorTextView, movieReview.getAuthor());
        setText(mContentTextView, movieReview.getContent());
    }
}
