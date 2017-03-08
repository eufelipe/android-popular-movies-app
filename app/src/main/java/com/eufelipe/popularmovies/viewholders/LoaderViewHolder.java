package com.eufelipe.popularmovies.viewholders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.eufelipe.popularmovies.R;

public class LoaderViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar mLoader;

    public LoaderViewHolder(View itemView) {
        super(itemView);
        mLoader = (ProgressBar) itemView.findViewById(R.id.loader);
        mLoader.setIndeterminate(true);
    }

}
