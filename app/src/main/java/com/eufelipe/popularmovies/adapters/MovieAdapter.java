package com.eufelipe.popularmovies.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.viewholders.LoaderViewHolder;
import com.eufelipe.popularmovies.viewholders.MovieViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> movieList = new ArrayList<>();


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADER = 1;
    private boolean isShowLoader = false;
    private boolean isShowAnimation = true;

    private final Context mContext;


    public MovieAdapter(Context context, List<Movie> movieList) {
        this.movieList = movieList;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_LOADER) {
            View layout = layoutInflater.inflate(R.layout.fragment_movie_list_loader, null);
            viewHolder = new LoaderViewHolder(layout);

        } else {
            View layout = layoutInflater.inflate(R.layout.fragment_movie_list_item, null);
            viewHolder = new MovieViewHolder(layout);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {

            if (isShowAnimation) {
                Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
                holder.itemView.startAnimation(animation);
            }

            if (movieList.size() > 0 && position < movieList.size()) {
                Movie movie = movieList.get(position);
                ((MovieViewHolder) holder).bind(movie);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (isShowLoader) ? this.movieList.size() + 1 : this.movieList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoader && position >= movieList.size()) {
            return VIEW_TYPE_LOADER;
        }
        return VIEW_TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        Movie listItem = movieList.get(position);
        return listItem.hashCode();
    }

    public void setIsShowLoader(boolean isShow) {
        this.isShowLoader = isShow;
    }

    public void setIsShowAnimation(boolean isShow) {
        this.isShowAnimation = isShow;
    }


    /**
     * @param position
     * @param movie
     * @desription : Método para add itens
     */
    public void addItem(int position, Movie movie) {

        if (movieList.contains(movie)) {
            return;
        }

        movieList.add(position, movie);
        notifyDataSetChanged();
    }

    /**
     * @return List<MoviesCallback>
     * @description : Método para pegar todos os registros atuais do adapter
     */
    public List<Movie> getItems() {
        if (movieList != null) {
            return movieList;
        }
        return new ArrayList<>();
    }


    /**
     * @param holder
     */
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public void clear() {
        movieList = new ArrayList<>();
    }
}
