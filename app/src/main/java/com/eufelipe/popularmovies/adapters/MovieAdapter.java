package com.eufelipe.popularmovies.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.viewholders.LoaderViewHolder;
import com.eufelipe.popularmovies.viewholders.MovieViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> movieList = new ArrayList<>();
    private Context mContext;
    private String[] items;

    public final int VIEW_TYPE_ITEM = 0;
    public final int VIEW_TYPE_LOADER = 1;
    private boolean isShowLoader = true;


    public MovieAdapter(Context mContext, List<Movie> movieList) {
        this.movieList = movieList;
        this.mContext = mContext;
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

        if (holder instanceof LoaderViewHolder) {
            ((LoaderViewHolder) holder).mLoader.setIndeterminate(true);

        } else if (movieList.size() > 0 && position < movieList.size()) {
            Movie movie = movieList.get(position);
            ((MovieViewHolder) holder).bind(movie);
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

    public void setIsShowLoader(boolean isShow) {
        this.isShowLoader = isShow;
    }


    /**
     * @param position
     * @param movie
     * @desription : Método para add itens
     */
    public void addItem(int position, Movie movie) {
        movieList.add(position, movie);
        Integer index = movieList.indexOf(movie);
//        notifyItemInserted(index);
        notifyDataSetChanged();
    }

    /**
     * @return List<Movie>
     * @description : Método para pegar todos os registros atuais do adapter
     */
    public List<Movie> getItems() {
        return movieList;
    }
}
