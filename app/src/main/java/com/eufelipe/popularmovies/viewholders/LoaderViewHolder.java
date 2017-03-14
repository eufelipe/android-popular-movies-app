package com.eufelipe.popularmovies.viewholders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.eufelipe.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.loader)
    ProgressBar mLoader;

    public LoaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mLoader.setIndeterminate(true);
    }
}
