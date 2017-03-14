package com.eufelipe.popularmovies.bases;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eufelipe.popularmovies.R;
import com.squareup.picasso.Picasso;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    protected BaseViewHolder(View itemView) {
        super(itemView);
    }


    /**
     * Helper para setar texto
     *
     * @param view
     * @param text
     */
    protected void setText(TextView view, String text) {
        if (view == null || text == null) {
            return;
        }
        view.setText(text);
    }

    /**
     * Helper para setar imagem
     *
     * @param context
     * @param image
     * @param imageView
     * @param defaultImage
     */
    private void setImage(Context context, String image, ImageView imageView, int defaultImage) {
        Picasso.with(context)
                .load(image)
                .error(defaultImage)
                .placeholder(defaultImage)
                .into(imageView);
    }

    /**
     * Sobrecarga
     *
     * @param context
     * @param image
     * @param imageView
     */
    protected void setImage(Context context, String image, ImageView imageView) {
        int defaultImage = R.mipmap.ic_movie_thumb;
        setImage(context, image, imageView, defaultImage);
    }


}
